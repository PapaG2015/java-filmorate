package ru.yandex.filmorate.service.film;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.filmorate.dao.FilmDao;
import ru.yandex.filmorate.model.Film;
import ru.yandex.filmorate.model.User;
import ru.yandex.filmorate.storage.film.FilmStorage;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class FilmDbService {
    @Autowired
    private FilmDao filmDoa;

    public void toLike(int filmID, int userID) {
        filmDoa.toLike(filmID, userID);
    }

    public void toDislike(int filmID, int userID) {
        filmDoa.toDislike(filmID, userID);
    }

    public List<Film> getRating(int filmAmount) {
        return filmDoa.getRating(filmAmount);
    }
}