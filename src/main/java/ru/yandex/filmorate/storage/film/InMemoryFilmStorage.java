package ru.yandex.filmorate.storage.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.filmorate.exception.IdException;
import ru.yandex.filmorate.model.Film;

import java.util.*;

@Component
@Slf4j
public class InMemoryFilmStorage implements FilmStorage {
    private int count = 0;

    private Map<Integer, Film> films = new HashMap<>();
    private Set<Film> filmSet = new TreeSet<>((f1, f2) -> {
        if (f1.getId() == f2.getId()) return 0;
        else if (f1.getRate() == f2.getRate()) return -1;
        else return -(f1.getRate() - f2.getRate());
    });

    @Override
    public Film get(int id) {
        return films.get(id);
    }

    @Override
    public Collection<Film> getAll() {
        return films.values();
    }

    @Override
    public Set<Film> getAllByRating() {
        return filmSet;
    }

    @Override
    public Film create(Film film) {

        int id = film.getId();

        if (films.containsKey(id)) {
            String s = "Another film has this ID";
            log.error(s);
            throw new IdException(s);
        }

        count++;
        id = count;
        film.setId(id);
        films.put(id, film);
        filmSet.add(film);
        log.info("Film adding: OK");
        return film;
    }

    @Override
    public Film change(Film film) {

        int id = film.getId();

        if (films.containsKey(id)) {
            filmSet.remove(films.get(id));

            films.put(id, film);
            filmSet.add(film);
            log.info("Film changing: OK");
            return films.get(id);
        } else {
            return create(film);
        }
    }

    @Override
    public void delete(Film film) {
        int id = film.getId();

        films.remove(id);
        filmSet.remove(film);
    }
}
