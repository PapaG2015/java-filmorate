package ru.yandex.filmorate.storage.film;

import ru.yandex.filmorate.model.Film;

import java.util.Collection;
import java.util.Set;

public interface FilmStorage {

    Film getFilm(int id);

    Collection<Film> getAllFilms();

    Set<Film> getAllFilmsByRating();

    Film createFilm(Film film);

    Film changeFilm(Film film);

}
