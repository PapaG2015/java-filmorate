package ru.yandex.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.filmorate.exception.IdException;
import ru.yandex.filmorate.model.Film;
import ru.yandex.filmorate.model.Genre;
import ru.yandex.filmorate.model.MPA;
import ru.yandex.filmorate.service.film.FilmDbService;
import ru.yandex.filmorate.storage.film.FilmStorage;

import javax.validation.constraints.Positive;
import java.util.Collection;

@Slf4j
@RestController
@RequestMapping("/mpa")
public class MPAController {

    @Autowired
    private FilmStorage filmDbStorage;
    @Autowired
    private FilmDbService filmDbService;

    @GetMapping
    public Collection<MPA> getAllMPA() {
        log.info("Get all MPA: OK");
        return filmDbStorage.getAllMPA();
    }

    @GetMapping("/{id}")
    public MPA getMPA(@PathVariable("id") @Positive int id ) {
        MPAValidate(id);
        log.info("Get mpa: OK");
        return filmDbStorage.getMPA(id);
    }

    private void MPAValidate(int id) {
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