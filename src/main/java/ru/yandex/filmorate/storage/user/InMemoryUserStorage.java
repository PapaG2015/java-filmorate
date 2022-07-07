package ru.yandex.filmorate.storage.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.filmorate.exception.IdException;
import ru.yandex.filmorate.model.User;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
public class InMemoryUserStorage implements UserStorage {
    private int count = 0;

    private Map<Integer, User> users = new HashMap<>();

    @Override
    public User get(int id) {
        return users.get(id);
    }

    @Override
    public Collection<User> getAll() {
        return users.values();
    }

    @Override
    public User create(User user) {

        int id = user.getId();

        if (users.containsKey(id)) {
            String s = "Another user has this ID";
            log.info(s);
            throw new IdException(s);
        }

        count++;
        id = count;
        user.setId(id);
        users.put(id, user);
        log.info("User adding: OK");
        return user;
    }

    @Override
    public User change(User user) {

        int id = user.getId();

        if (users.containsKey(id)) {
            users.put(id, user);
            log.info("User changing: OK");
            return users.get(id);
        }
        else {
            return create(user);
        }
    }

    @Override
    public void delete(User user) {
        int id = user.getId();
        users.remove(id);
    }
}
