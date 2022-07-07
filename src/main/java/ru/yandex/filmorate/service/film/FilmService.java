package ru.yandex.filmorate.service.film;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.filmorate.model.Film;
import ru.yandex.filmorate.model.User;
import ru.yandex.filmorate.storage.film.FilmStorage;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class FilmService {
    @Autowired
    private FilmStorage filmStorage;
    @Autowired
    private FilmStorage useStorage;


    public void toLike(int filmID, int userID) {
        Film film = filmStorage.get(filmID);
        film.getLikes().add(userID);
        film.setRate(film.getRate() + 1);
        filmStorage.change(film);
    }

    public void toDislike(int filmID, int userID) {
        Film film = filmStorage.get(filmID);
        film.getLikes().remove(userID);
        film.setRate(film.getRate() - 1);
        filmStorage.change(film);
    }

    public List<Film> getRating(int filmAmount) {
        return filmStorage.getAllByRating().stream().limit(filmAmount).collect(Collectors.toList());
    }
}
