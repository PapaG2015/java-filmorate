package ru.yandex.filmorate.dao;

import ru.yandex.filmorate.model.Film;
import ru.yandex.filmorate.model.Genre;
import ru.yandex.filmorate.model.MPA;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface FilmDao {
    Optional<Film> get(int id);

    void toLike(int filmID, int userID);

    void toDislike(int filmID, int userID);

    Collection<Film> getAll();

    List<Film> getRating(int filmAmount);

    Optional<Film> create(Film film);

    Optional<Film> change(Film film);

    void delete(Film film);

    Collection<Genre> getAllGenres();

    Genre getGenre(int id);

    Collection<MPA> getAllMPA();

    MPA getMPA(int id);
}
