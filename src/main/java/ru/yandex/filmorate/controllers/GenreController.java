package ru.yandex.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.filmorate.exception.IdException;
import ru.yandex.filmorate.model.Film;
import ru.yandex.filmorate.model.Genre;
import ru.yandex.filmorate.service.film.FilmDbService;
import ru.yandex.filmorate.storage.film.FilmStorage;

import javax.validation.constraints.Positive;
import java.util.Collection;

@Slf4j
@RestController
@RequestMapping("/genres")
public class GenreController {

    @Autowired
    private FilmStorage filmDbStorage;
    @Autowired
    private FilmDbService filmDbService;

    @GetMapping
    public Collection<Genre> getAllGenres() {
        log.info("Get all genres: OK");
        return filmDbStorage.getAllGenres();
    }

    @GetMapping("/{id}")
    public Genre getGenre(@PathVariable("id") @Positive int id) {
        genreValidate(id);
        log.info("Get genre: OK");
        return filmDbStorage.getGenre(id);
    }

    private void genreValidate(int id) {
        if (id < 0) {
            log.error("ID < 0");
            throw new IdException("ID < 0");
        }
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleIdException(IdException e) {
        log.error(e.getMessage());
        return e.getMessage();
    }
}