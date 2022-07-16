package ru.yandex.filmorate.service.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.filmorate.dao.UserDao;
import ru.yandex.filmorate.model.User;
import ru.yandex.filmorate.storage.user.UserStorage;

import javax.validation.constraints.AssertFalse;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@Service
public class UserDbService {

    @Autowired
    private UserDao userDao;

    public void addFriend(int id, int friendId) {
        userDao.addFriend(id, friendId);
    }

    public void deleteFriend(int id, int friendId) {
        userDao.deleteFriend(id, friendId);
    }

    public List<User> getMyFriends(int id) {
        return userDao.getMyFriends(id);
    }

    public List<User> getCommonFriends(int id, int otherId) {
        return userDao.getCommonFriends(id, otherId);
    }
}
