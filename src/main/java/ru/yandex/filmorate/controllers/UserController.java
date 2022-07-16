package ru.yandex.filmorate.controllers;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.filmorate.exception.DateException;
import ru.yandex.filmorate.exception.IdException;
import ru.yandex.filmorate.service.user.UserDbService;
import ru.yandex.filmorate.storage.user.UserStorage;
import ru.yandex.filmorate.model.User;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@RestController
@Data
@Slf4j
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserStorage userDbStorage;
    @Autowired
    private UserDbService userDbService;

    @GetMapping("/{id}")
    public Optional<User> getUser(@PathVariable("id") int id) {
        if (id <= 0) {
            log.error("ID must be > 0");
            throw new IdException("ID must be > 0");
        } else {
            log.info("Get film: OK");
            return userDbStorage.get(id);
        }
    }

    //добавление в друзья
    @PutMapping("/{id}/friends/{friendId}")
    public Optional<User> addFriend(@PathVariable("id") int id, @PathVariable("friendId") int friendId) {
        if (friendId <= 0) {
            log.error("ID must be > 0");
            throw new IdException("ID must be > 0");
        } else {
            userDbService.addFriend(id, friendId);
            log.info("Add friend: OK");
            return userDbStorage.get(id);
        }
    }

    //удаление из друзей
    @DeleteMapping("/{id}/friends/{friendId}")
    public Optional<User> deleteFriend(@PathVariable("id") int id, @PathVariable("friendId") int friendId) {
        userDbService.deleteFriend(id, friendId);
        log.info("Delete friend: OK");
        return userDbStorage.get(id);
    }

    //возвращаем список пользователей, являющихся его друзьями
    @GetMapping("/{id}/friends")
    public List<User> getMyFriends(@PathVariable("id") int id) {
        log.info("Get my friends: OK");
        return userDbService.getMyFriends(id);
    }

    //список друзей, общих с другим пользователем
    @GetMapping("/{id}/friends/common/{otherId}")
    public List<User> getCommonFriends(@PathVariable("id") int id, @PathVariable("otherId") int otherId) {
        log.info("Get common friends: OK");
        return userDbService.getCommonFriends(id, otherId);
    }

    @GetMapping
    public Collection<User> getAllUsers() {
        log.info("Get all users: OK");
        return userDbStorage.getAll();
    }

    //POST
    //если такой пользователь уже существует, то сервер вернёт ошибку
    @PostMapping
    public User createUser(@Valid @RequestBody User user) {
        userValidate(user);
        log.info("Create user: OK");
        return userDbStorage.create(user);
    }

    //PUT
    //Если же такого пользователя на сервере нет, то он будет добавлен.
    @PutMapping
    public User changeUser(@Valid @RequestBody User user) {
        userValidate(user);
        log.info("Change user: OK");
        return userDbStorage.change(user);
    }

    private void userValidate(User user) {
        String s;

        if (user.getName().isEmpty()) {
            s = "Name will be equal login";
            log.info(s);
            user.setName(user.getLogin());
        }

        if (user.getBirthday().isAfter(LocalDate.now())) {
            s = "You are from future";
            log.error(s);
            throw new DateException(s);
        }

        if (user.getId() < 0) {
            log.error("ID < 0");
            throw new IdException("ID < 0");
        }
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleBirthdayException(DateException e) {
        log.error(e.getMessage());
        return e.getMessage();
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleIdException(IdException e) {
        log.error(e.getMessage());
        return e.getMessage();
    }


}
