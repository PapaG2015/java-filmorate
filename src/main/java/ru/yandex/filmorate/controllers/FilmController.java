package ru.yandex.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.filmorate.exception.DateException;
import ru.yandex.filmorate.exception.IdException;
import ru.yandex.filmorate.model.Film;
import ru.yandex.filmorate.service.film.FilmService;
import ru.yandex.filmorate.storage.film.FilmStorage;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {

    @Autowired
    private FilmStorage filmStorage;
    @Autowired
    private FilmService filmService;

    @GetMapping("/{id}")
    public Film getFilm(@PathVariable("id") int id) {
        if (id <= 0) {
            log.error("ID must be > 0");
            throw new IdException("ID must be > 0");
        } else {
            log.info("Get film: OK");
            return filmStorage.get(id);
        }
    }

    //пользователь ставит лайк фильму
    @PutMapping("/{id}/like/{userId}")
    public Film toLike(@PathVariable("id") int id, @PathVariable("userId") int userId) {
        filmService.toLike(id, userId);
        log.info("Like film: OK");
        return filmStorage.get(id);
    }

    // пользователь удаляет лайк
    @DeleteMapping("/{id}/like/{userId}")
    public Film toDislike(@PathVariable("id") int id, @PathVariable("userId") int userId) {
        if (userId <= 0) {
            log.error("ID must be > 0");
            throw new IdException("ID must be > 0");
        } else {
            filmService.toDislike(id, userId);
            log.info("Delete like film: OK");
            return filmStorage.get(id);
        }
    }

    @GetMapping
    public Collection<Film> getAllFilms() {
        log.info("Get all films: OK");
        return filmStorage.getAll();
    }

    //возвращает список из первых count фильмов по количеству лайков
    @GetMapping("/popular")
    public List<Film> getRating(@RequestParam(required = false, name = "count", defaultValue = "10") int count) {
        log.info("Get popular films: OK");
        return filmService.getRating(count);
    }

    //POST
    //если такой film уже существует, то сервер вернёт ошибку
    @PostMapping
    public Film createFilm(@Valid @RequestBody Film film) {
        filmValidate(film);
        log.info("Create film: OK");
        return filmStorage.create(film);
    }

    //PUT
    //Если же такого film на сервере нет, то он будет добавлен.
    @PutMapping
    public Film changeFilm(@Valid @RequestBody Film film) {
        filmValidate(film);
        log.info("Chanhe film: OK");
        return filmStorage.change(film);
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleIdException(IdException e) {
        log.error(e.getMessage());
        return e.getMessage();
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleReleaseDateException(DateException e) {
        log.error(e.getMessage());
        return e.getMessage();
    }

    private void filmValidate(Film film) {
        String s;

        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            s = "Date of release must after 27.12.1985";
            log.error(s);
            throw new DateException(s);
        }

        if (film.getId() < 0) {
            log.error("ID < 0");
            throw new IdException("ID < 0");
        }
    }
}
