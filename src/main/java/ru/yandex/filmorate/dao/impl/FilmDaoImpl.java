package ru.yandex.filmorate.dao.impl;

import lombok.extern.slf4j.Slf4j;
import org.apache.el.stream.Stream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.filmorate.dao.FilmDao;
import ru.yandex.filmorate.model.Film;
import ru.yandex.filmorate.model.Genre;
import ru.yandex.filmorate.model.MPA;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Component
public class FilmDaoImpl implements FilmDao {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public Optional<Film> get(int id) {
        SqlRowSet filmRows = jdbcTemplate.queryForRowSet("select * from films " +
                "LEFT OUTER JOIN ratings ON films.rating = ratings.id " + "where films.id = ?"
                , id);

        if (filmRows.next()) {
            Film film = new Film(
                    filmRows.getInt("id"),
                    filmRows.getString("name"),
                    filmRows.getString("description"),
                    filmRows.getDate("releaseDate").toLocalDate(),
                    filmRows.getInt("duration"),
                    new MPA(filmRows.getInt("rating"), filmRows.getString("mpa")));

            String genreRows = "select * from films_by_genre " +
                    "LEFT OUTER JOIN genres ON films_by_genre.genre_id = genres.id " +
                    "where film_id = ?";
            List<Pair<Integer, String>> list = jdbcTemplate.query(genreRows, (rs, rowNum) ->
                    Pair.of(rs.getInt("genre_id"), rs.getString("description")), id);

            if (!list.isEmpty()) film.setGenres(list.stream().map(pair -> new Genre(pair.getFirst(), pair.getSecond())).collect(Collectors.toSet()));

            log.info("Найден фильм с идентификатором {} по имени {}", film.getId(), film.getName());

            return Optional.of(film);
        } else {
            log.info("Фильм с идентификатором {} не найден.", id);
            return Optional.empty();
        }
    }

    @Override
    public void toLike(int filmID, int userID) {
        String sqlQuery = "insert into likes(film_id, user_id) " + "values (?, ?)";
        jdbcTemplate.update(sqlQuery, filmID, userID);
    }

    @Override
    public void toDislike(int filmID, int userID) {
        String sqlQuery = "delete from likes where film_id = ? and user_id = ?";
        jdbcTemplate.update(sqlQuery, filmID, userID);
    }

    @Override
    public Collection<Film> getAll() {
        String sql = "select * from films "  + "LEFT OUTER JOIN ratings ON films.rating = ratings.id";

        return jdbcTemplate.query(sql, (rs, rowNum) -> makeFilm(rs));
    }

    private Film makeFilm(ResultSet rs) throws SQLException {
        Film film = new Film(
                rs.getInt("id"),
                rs.getString("name"),
                rs.getString("description"),
                rs.getDate("releaseDate").toLocalDate(),
                rs.getInt("duration"),
                new MPA(rs.getInt("rating"), rs.getString("mpa")));

        return film;
    }

    @Override
    public List<Film> getRating(int filmAmount) {
        String sql = "SELECT f.*, COUNT(li.user_id) AS p, ratings.mpa " +
                "FROM films AS f " +
                "LEFT OUTER JOIN ratings ON f.rating = ratings.id " +
                "LEFT OUTER JOIN likes AS li ON f.id = li.film_id " +
                "GROUP BY f.id " +
                "ORDER BY p DESC " +
                "LIMIT ?";

        return jdbcTemplate.query(sql, (rs, rowNum) -> makeFilm(rs), filmAmount);
    }

    @Override
    public Optional<Film> create(Film film) {
        String sqlQuery = "insert into films(name, description, releasedate , duration, rating) " + "values " + "(?, ?, ?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sqlQuery, new String[]{"id"});
            stmt.setString(1, film.getName());
            stmt.setString(2, film.getDescription());
            stmt.setDate(3, Date.valueOf(film.getReleaseDate()));
            stmt.setLong(4, film.getDuration());
            stmt.setInt(5, film.getMpa().getId());
            return stmt;
        }, keyHolder);
        int id = keyHolder.getKey().intValue();

        for (Genre g : film.getGenres()) {
            String sqlQueryGenres = "insert into films_by_genre(film_id, genre_id) " + "values (?, ?)";
            jdbcTemplate.update(sqlQueryGenres, id, g.getId());
        }

        return get(id);
    }

    @Override
    public Optional<Film> change(Film film) {
        Optional<Film> filmFrom = get(film.getId());

        if (filmFrom.isEmpty()) return create(film);
        else {
            int id = film.getId();

            String sqlQueryLogins = "update films set " +
                    "name = ? ," + "description = ? ," + "releasedate = ? ," + "duration = ? ," + "rating = ? " + "where id = ?";
            jdbcTemplate.update(sqlQueryLogins, film.getName(), film.getDescription(), film.getReleaseDate(), film.getDuration(),
                    film.getMpa().getId(), id);

            //удаление жанров
            String sqlQuery = "delete from films_by_genre where film_id = ?";
            jdbcTemplate.update(sqlQuery, id);

            //добавдение жанров
            for (Genre genre : film.getGenres()) {
                int genreId = genre.getId();

                String sqlQueryGenre = "insert into films_by_genre(film_id, genre_id) " + "values (?, ?)";
                jdbcTemplate.update(sqlQueryGenre, id, genreId);
            }
            return get(id);
        }
    }

    @Override
    public void delete(Film film) {
    }

    @Override
    public Collection<Genre> getAllGenres() {
        String sql = "select * from genres";


        List<Genre> list = jdbcTemplate.query(sql, (rs, rowNum) -> new Genre(rs.getInt("id"), rs.getString("description")));
        list.sort(
                (genre1, genre2) -> {
                    return genre1.getId() - genre2.getId();
                }
        );
        return list;
    }

    @Override
    public Genre getGenre(int id) {
        String sql = "select * from genres where id = ?";

        return jdbcTemplate.query(sql, (rs, rowNum) -> new Genre(rs.getInt("id"), rs.getString("description")), id).get(0);
    }

    @Override
    public Collection<MPA> getAllMPA() {
        String sql = "select * from ratings";

        return jdbcTemplate.query(sql, (rs, rowNum) -> new MPA(rs.getInt("id"), rs.getString("mpa")));
    }

    @Override
    public MPA getMPA(int id) {
        String sql = "select * from ratings where id = ?";

        return jdbcTemplate.query(sql, (rs, rowNum) -> new MPA(rs.getInt("id"), rs.getString("mpa")), id).get(0);
    }
}
