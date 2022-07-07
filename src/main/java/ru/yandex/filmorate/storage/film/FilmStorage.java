package ru.yandex.filmorate.storage.film;

import ru.yandex.filmorate.model.Film;

import java.util.Collection;
import java.util.Set;

public interface FilmStorage {

    Film get(int id);

    Collection<Film> getAll();

    Set<Film> getAllByRating();

    Film create(Film film);

    Film change(Film film);

    void delete(Film film);

}
