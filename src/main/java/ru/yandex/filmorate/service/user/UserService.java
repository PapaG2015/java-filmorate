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

    /*@Autowired
    private UserStorage userStorage;

    public void addFriend(int id, int friendId) {
        userStorage.get(id).getFriends().add(friendId);
        userStorage.get(friendId).getFriends().add(id);
    }

    public void deleteFriend(int id, int friendId) {
        userStorage.get(id).getFriends().remove(friendId);
        userStorage.get(friendId).getFriends().remove(id);
    }

    public List<User> getMyFriends(int id) {
        List<User> myFriends = new ArrayList<>();
        for (int i : userStorage.get(id).getFriends()) {
            myFriends.add(userStorage.get(i));
        }
        return myFriends;
    }

    public List<User> getCommonFriends(int id, int otherId) {
        List<User> commonFriends = new ArrayList<>();

        try {
            userStorage.get(id).getFriends().stream().filter(friendID ->
                    userStorage.get(otherId).getFriends().contains(friendID)).forEach(friendID ->
                    commonFriends.add(userStorage.get(friendID)));

            return commonFriends;
        } catch (NullPointerException e) {
            return commonFriends;
        }
    }*/
}
