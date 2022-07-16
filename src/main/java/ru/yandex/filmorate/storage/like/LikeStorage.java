package ru.yandex.filmorate.storage.like;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.yandex.filmorate.dao.LikeDao;

@Component
public class LikeStorage {
    @Autowired
    private LikeDao likeDao;

    public void toLike(int filmID, int userID) {
        likeDao.toLike(filmID, userID);
    }

    public void toDislike(int filmID, int userID) {
        likeDao.toDislike(filmID, userID);
    }
}
