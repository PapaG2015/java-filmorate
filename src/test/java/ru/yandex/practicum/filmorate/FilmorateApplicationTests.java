package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.controllers.*;
import ru.yandex.practicum.filmorate.model.*;
import ru.yandex.practicum.filmorate.exception.*;
import org.junit.jupiter.api.function.Executable;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class FilmorateApplicationTests {

    @Test
    void checkFilmWithNoName() {
        Film film = Film.builder()
                .name("")
                .description("test")
                .releaseDate(LocalDate.of(2000, 12, 12))
                .duration(199)
                .build();
        FilmController filmController = new FilmController();
        try {
            filmController.createFilm(film);
        } catch (ValidationException | SimilarException e) {
            assertEquals("Name is needed", e.getMessage());
        }
    }

    @Test
    void checkFilmDate() {
        Film film = Film.builder()
                .name("test")
                .description("test")
                .releaseDate(LocalDate.of(1000, 12, 12))
                .duration(199)
                .build();
        FilmController filmController = new FilmController();
        try {
            filmController.createFilm(film);
        } catch (ValidationException | SimilarException e) {
            assertEquals("Date of release must after 27.12.1985", e.getMessage());
        }
    }

    @Test
    void checkUserEmail() {
        User user = User.builder()
                .email("@")
                .login("marat")
                .name("marat")
                .birthday(LocalDate.of(3000, 12, 12))
                .build();
        UserController userController = new UserController();
        try {
            userController.createUser(user);
        } catch (ValidationException | SimilarException e) {
            assertEquals("Bad email. Example xxx@xxx.ru", e.getMessage());
        }
    }

    @Test
    void checkUserBirthday() {
        User user = User.builder()
                .email("marat@yandex.ru")
                .login("marat")
                .name("marat")
                .birthday(LocalDate.of(3000, 12, 12))
                .build();
        UserController userController = new UserController();
        try {
            userController.createUser(user);
        } catch (ValidationException | SimilarException e) {
            assertEquals("You are from future", e.getMessage());
        }
    }

}
