package ru.yandex.filmorate.storage.film;

import org.springframework.web.bind.annotation.PathVariable;
import ru.yandex.filmorate.model.Film;
import ru.yandex.filmorate.model.Genre;
import ru.yandex.filmorate.model.MPA;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;

public interface FilmStorage {

    Optional<Film> get(int id);

    Collection<Film> getAll();

    Optional<Film> create(Film film);

    Optional<Film> change(Film film);

    void delete(Film film);

    Collection<Genre> getAllGenres();

    Genre getGenre(int id);

    Collection<MPA> getAllMPA();

    MPA getMPA(int id);
}
