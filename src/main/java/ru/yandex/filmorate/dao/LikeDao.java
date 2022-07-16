package ru.yandex.filmorate.dao;

public interface LikeDao {
    void toLike(int filmID, int userID);

    void toDislike(int filmID, int userID);
}

