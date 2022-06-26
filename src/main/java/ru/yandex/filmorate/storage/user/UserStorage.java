package ru.yandex.filmorate.storage.user;

import ru.yandex.filmorate.model.User;

import java.util.Collection;

public interface UserStorage {

    User getUser(int id);

    Collection<User> getAllUsers();

    User createUser(User user);

    User changeUser(User user);

}