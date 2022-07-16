package ru.yandex.filmorate.storage.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.yandex.filmorate.dao.UserDao;
import ru.yandex.filmorate.model.User;

import java.util.Collection;
import java.util.Optional;

@Component("userDbStorage")
public class UserDbStorage implements UserStorage {
    @Autowired
    UserDao userDao;

    @Override
    public Optional<User> get(int id) {
        return userDao.get(id);
    }

    @Override
    public Collection<User> getAll() {
        return userDao.getAll();
    }

    @Override
    public User create(User user) {
        return userDao.create(user);
    };

    @Override
    public User change(User user) {
        return userDao.change(user);
    };

    @Override
    public void delete(User user) {

    }
}
