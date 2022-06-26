package ru.yandex.filmorate.storage.user;

import ru.yandex.filmorate.model.User;

import java.util.Collection;

public interface UserStorage {

    User get(int id);

    Collection<User> getAll();

    User create(User user);

    User change(User user);

    void delete(User user);

}