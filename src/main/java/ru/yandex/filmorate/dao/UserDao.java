package ru.yandex.filmorate.dao;

import ru.yandex.filmorate.model.User;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface UserDao {
    Optional<User> get(int id);

    void addFriend(int id, int friendId);

    void deleteFriend(int id, int friendId);

    List<User> getMyFriends(int id);

    List<User> getCommonFriends(int id, int otherId);

    Collection<User> getAll();

    User create(User user);

    User change(User user);

    void delete(User user);
}
