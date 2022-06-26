package ru.yandex.filmorate.service.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.filmorate.model.User;
import ru.yandex.filmorate.storage.user.UserStorage;

import javax.validation.constraints.AssertFalse;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private UserStorage userStorage;

    public void addFriend(int id, int friendId) {
        userStorage.getUser(id).getFriends().add(friendId);
        userStorage.getUser(friendId).getFriends().add(id);
    }

    public void deleteFriend(int id, int friendId) {
        userStorage.getUser(id).getFriends().remove(friendId);
        userStorage.getUser(friendId).getFriends().remove(id);
    }

    public List<User> getMyFriends(int id) {
        List<User> myFriends = new ArrayList<>();
        for (int i : userStorage.getUser(id).getFriends()) {
            myFriends.add(userStorage.getUser(i));
        }
        return myFriends;
    }

    public List<User> getCommonFriends(int id, int otherId) {
        List<User> commonFriends = new ArrayList<>();

        try {
            userStorage.getUser(id).getFriends().stream().filter(friendID ->
                    userStorage.getUser(otherId).getFriends().contains(friendID)).forEach(friendID ->
                    commonFriends.add(userStorage.getUser(friendID)));

            return commonFriends;
        } catch (NullPointerException e) {
            return commonFriends;
        }
    }
}
