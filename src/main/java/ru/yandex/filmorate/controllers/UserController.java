package ru.yandex.filmorate.controllers;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.filmorate.exception.DateException;
import ru.yandex.filmorate.exception.IdException;
import ru.yandex.filmorate.service.user.UserService;
import ru.yandex.filmorate.storage.user.UserStorage;
import ru.yandex.filmorate.model.User;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

@RestController
@Data
@Slf4j
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserStorage userStorage;
    @Autowired
    private UserService userService;

    @GetMapping("/{id}")
    public User getUser(@PathVariable("id") int id) {
        if (id <= 0) {
            log.error("ID must be > 0");
            throw new IdException("ID must be > 0");
        } else {
            log.info("Get film: OK");
            return userStorage.get(id);
        }
    }

    //добавление в друзья
    @PutMapping("/{id}/friends/{friendId}")
    public User addFriend(@PathVariable("id") int id, @PathVariable("friendId") int friendId) {
        if (friendId <= 0) {
            log.error("ID must be > 0");
            throw new IdException("ID must be > 0");
        } else {
            userService.addFriend(id, friendId);
            log.info("Add friend: OK");
            return userStorage.get(id);
        }
    }

    //удаление из друзей
    @DeleteMapping("/{id}/friends/{friendId}")
    public User deleteFriend(@PathVariable("id") int id, @PathVariable("friendId") int friendId) {
        userService.deleteFriend(id, friendId);
        log.info("Delete friend: OK");
        return userStorage.get(id);
    }

    //возвращаем список пользователей, являющихся его друзьями
    @GetMapping("/{id}/friends")
    public List<User> getMyFriends(@PathVariable("id") int id) {
        log.info("Get my friends: OK");
        return userService.getMyFriends(id);
    }

    //список друзей, общих с другим пользователем
    @GetMapping("/{id}/friends/common/{otherId}")
    public List<User> getCommonFriends(@PathVariable("id") int id, @PathVariable("otherId") int otherId) {
        log.info("Get common friends: OK");
        return userService.getCommonFriends(id, otherId);
    }

    @GetMapping
    public Collection<User> getAllUsers() {
        log.info("Get all users: OK");
        return userStorage.getAll();
    }

    //POST
    //если такой пользователь уже существует, то сервер вернёт ошибку
    @PostMapping
    public User createUser(@Valid @RequestBody User user) {
        userValidate(user);
        log.info("Create user: OK");
        return userStorage.create(user);
    }

    //PUT
    //Если же такого пользователя на сервере нет, то он будет добавлен.
    @PutMapping
    public User changeUser(@Valid @RequestBody User user) {
        userValidate(user);
        log.info("Change user: OK");
        return userStorage.change(user);
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
