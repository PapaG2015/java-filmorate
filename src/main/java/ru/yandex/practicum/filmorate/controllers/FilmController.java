package ru.yandex.practicum.filmorate.controllers;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.IdException;
import ru.yandex.practicum.filmorate.exception.SimilarException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.web.bind.MethodArgumentNotValidException;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.*;

@RestController
@Data
@Slf4j
@RequestMapping("/films")
public class FilmController {
    private static int count = 0;

    //private static final Logger log = LoggerFactory.getLogger(FilmController.class);

    private Map<Integer, Film> films = new HashMap<>();
    private List<Film> filmsList = new ArrayList<>();

    @GetMapping
    public List<Film> getAllFilms() {
        for (Integer i : films.keySet()) {
            filmsList.add(films.get(i));
        }
        return filmsList;
    }

    //POST
    //если такой film уже существует, то сервер вернёт ошибку
    @PostMapping
    public Film createFilm(@Valid @RequestBody Film film) throws SimilarException, ValidationException {
        validate(film);

        int id = film.getId();

        if (films.containsKey(id)) {
            String s = "Another film has this ID";
            log.error(s);
            throw new SimilarException(s);
        }

        /*id = new Random().nextInt(1000) + 1;

        while (films.containsKey(id)) {
            id = new Random().nextInt(1000) + 1;
        }*/

        count++;
        id = count;
        film.setId(id);
        films.put(id, film);
        log.info("Film adding: OK");
        return film;
    }

    //PUT
    //Если же такого film на сервере нет, то он будет добавлен.
    @PutMapping
    public Film changeFilm(@Valid @RequestBody Film film) throws SimilarException, ValidationException, IdException {
        validate(film);

        int id = film.getId();

        if (id < 0) {
            throw  new IdException("ID < 0");
        }

        if (films.containsKey(id)) {
            films.put(id, film);
            log.info("Film changing: OK");
            return films.get(id);
        } else {
            return createFilm(film);
        }
    }

    @ExceptionHandler(SimilarException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleException(SimilarException e) {
        return e.getMessage();
    }

    @ExceptionHandler(ValidationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleException(ValidationException e) {
        return e.getMessage();
    }

    @ExceptionHandler(IdException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String handleException(IdException e) {
        return e.getMessage();
    }

    /*@ExceptionHandler(MethodArgumentNotValidException.class)
    public String handleException(MethodArgumentNotValidException e) {
        return e.getMessage();
    }*/


    private void validate(Film film) throws ValidationException {
        String s;
        if (film.getName().isEmpty()) {
            s = "Name is needed";
            log.error(s);
            throw new ValidationException(s);
        }
        if (film.getDescription().length() > 200) {
            s = "Description is too long (must be <= 200)";
            log.error(s);
            throw new ValidationException(s);
        }
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            s = "Date of release must after 27.12.1985";
            log.error(s);
            throw new ValidationException(s);
        }
        if (film.getDuration() < 0 | film.getDuration() == 0) {
            s = "Duration of film must be positive";
            log.error(s);
            throw new ValidationException(s);
        }
    }
}
