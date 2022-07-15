package ru.yandex.filmorate.storage.film;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import ru.yandex.filmorate.dao.FilmDao;
import ru.yandex.filmorate.model.Film;
import ru.yandex.filmorate.model.Genre;
import ru.yandex.filmorate.model.MPA;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;

@Component("filmDbStorage")
public class FilmDbStorage implements FilmStorage {
    @Autowired
    private FilmDao filmDao;

    @Override
    public Optional<Film> get(int id) {
        return filmDao.get(id);
    };

    @Override
    public Collection<Film> getAll() {
        return filmDao.getAll();
    };

    @Override
    public Optional<Film> create(Film film) {
        return filmDao.create(film);
    }

    @Override
    public Optional<Film> change(Film film) {
        return filmDao.change(film);
    }

    @Override
    public void delete(Film film) {
    }

    @Override
    public Collection<Genre> getAllGenres() {
        return filmDao.getAllGenres();
    }

    @Override
    public Genre getGenre(int id) {
        return filmDao.getGenre(id);
    }

    @Override
    public Collection<MPA> getAllMPA() {
        return filmDao.getAllMPA();
    }

    @Override
    public MPA getMPA(int id) {
        return filmDao.getMPA(id);
    }
}
