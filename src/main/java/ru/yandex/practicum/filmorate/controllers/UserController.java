package ru.yandex.practicum.filmorate.controllers;

import lombok.Data;
import lombok.ToString;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.SimilarException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.exception.IdException;
import ru.yandex.practicum.filmorate.model.User;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.List;
import java.util.ArrayList;

@RestController
@Data
public class UserController {
    private static int count = 0;

    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    private Map<Integer, User> users = new HashMap<>();
    private List<User> usersList = new ArrayList<>();

    @GetMapping("/users")
    public List<User> getAllUsers() {
        for (Integer i : users.keySet()) {
            usersList.add(users.get(i));
        }
        return usersList;
    }

    //POST
    //если такой пользователь уже существует, то сервер вернёт ошибку
    @PostMapping("/users")
    public User createUser(@RequestBody User user) throws SimilarException, ValidationException {
        validate(user);

        int id = user.getId();

        if (users.containsKey(id)) {
            String s = "Another user has this ID";
            log.info(s);
            throw new SimilarException(s);
        }

        /*id = new Random().nextInt(1000) + 1;

        while (users.containsKey(id)) {
            id = new Random().nextInt(1000) + 1;
        }*/

        count++;
        id = count;
        user.setId(id);
        users.put(id, user);
        log.info("User adding: OK");
        return user;
    }

    //PUT
    //Если же такого пользователя на сервере нет, то он будет добавлен.
    @PutMapping("/users")
    public User changeUser(@RequestBody User user) throws ValidationException, SimilarException, IdException {
        validate(user);

        int id = user.getId();

        if (id < 0) {
            throw  new IdException("ID < 0");
        }

        if (users.containsKey(id)) {
            users.put(id, user);
            log.info("User changing: OK");
            return users.get(id);
        }
        else {
            return createUser(user);
        }
    }

    private void validate(User user) throws ValidationException {
        String s;
        if (user.getEmail().isEmpty() | !user.getEmail().matches("\\w+@[a-z]+\\.[a-z]+")) {
            s = "Bad email. Example xxx@xxx.ru";
            log.error(s);
            throw new ValidationException(s);
        }
        if (user.getLogin().isEmpty() | user.getLogin().contains(" ")) {
            s = "Bad login: empty or with whitespace";
            log.error(s);
            throw new ValidationException(s);
        }
        if (user.getName().isEmpty()) {
            s = "Name will be equal login";
            log.info(s);
            user.setName(user.getLogin());
        }
        if (user.getBirthday().isAfter(LocalDate.now())) {
            s = "You are from future";
            log.error(s);
            throw new ValidationException(s);
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

}
