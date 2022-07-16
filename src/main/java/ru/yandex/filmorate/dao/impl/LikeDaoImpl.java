package ru.yandex.filmorate.dao.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.filmorate.dao.LikeDao;

@Component
public class LikeDaoImpl implements LikeDao {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public void toLike(int filmID, int userID) {
        String sqlQuery = "insert into likes(film_id, user_id) " + "values (?, ?)";
        jdbcTemplate.update(sqlQuery, filmID, userID);
    }

    @Override
    public void toDislike(int filmID, int userID) {
        String sqlQuery = "delete from likes where film_id = ? and user_id = ?";
        jdbcTemplate.update(sqlQuery, filmID, userID);
    }
}
