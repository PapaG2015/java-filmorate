package ru.yandex.filmorate.dao.impl;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.filmorate.dao.UserDao;
import ru.yandex.filmorate.model.Film;
import ru.yandex.filmorate.model.User;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Component
public class UserDaoImpl implements UserDao {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public Optional<User> get(int id) {
        SqlRowSet userRows = jdbcTemplate.queryForRowSet("select * from users where id = ?", id);

        if (userRows.next()) {
            User user = new User(
                    userRows.getInt("id"),
                    userRows.getString("name"),
                    userRows.getDate("birthday").toLocalDate());

            //запрос почты
            String sqlEmail = "select * from emails where id = ?";
            String email = jdbcTemplate.query(sqlEmail, (rs, rowNum) -> rs.getString("email"), id).get(0);
            user.setEmail(email);

            //запрос логина
            String sqlLogin = "select * from logins where id = ?";
            String login = jdbcTemplate.query(sqlLogin, (rs, rowNum) -> rs.getString("login"), id).get(0);
            user.setLogin(login);

            log.info("Найден пользователь с идентификатором {} с логином {}", user.getId(), user.getLogin());

            return Optional.of(user);
        } else {
            log.info("Пользователь с идентификатором {} не найден.", id);
            return Optional.empty();
        }
    }

    @Override
    public void addFriend(int id, int friendId) {
        String sqlQuery = "insert into friends(user_id, friend_id, friendship_status) " + "values (?, ?, ?)";
        jdbcTemplate.update(sqlQuery, id, friendId, true);

        jdbcTemplate.update(sqlQuery, friendId, id, false);
    }

    @Override
    public void deleteFriend(int id, int friendId) {
        String sqlQuery = "delete from friends where user_id = ? and friend_id = ?";
        jdbcTemplate.update(sqlQuery, id, friendId);
    }

    @Override
    public List<User> getMyFriends(int id) {
        String sql = "select * from friends where user_id = ? and friendship_status = TRUE";

        /*if (jdbcTemplate.query(sql, (rs, rowNum) -> makeUser(rs), id).isEmpty()) {
            System.out.println("qqqqqqqqqqqqqqqqqq");
            return List.of();
        }*/

        return jdbcTemplate.query(sql, (rs, rowNum) -> makeUser(rs), id).stream().map(Optional::get).collect(Collectors.toList());
    }

    private Optional<User> makeUser(ResultSet rs) throws SQLException {
        int id = rs.getInt("friend_id");

        return get(id);
    }

    public List<User> getCommonFriends(int id, int otherId) {
        List<User> listOfId = getMyFriends(id);
        List<User> listOfOtherId = getMyFriends(otherId);

        List<User> listCommonFriends = new ArrayList<>();

        listOfOtherId.stream().filter(user -> listOfOtherId.contains(user)).forEach(user -> listCommonFriends.add(user));
        return listCommonFriends;
    }

    @Override
    public Collection<User> getAll() {
        String sql = "select * from users";

        return jdbcTemplate.query(sql, (rs, rowNum) -> get(rs.getInt("id"))).stream().map(Optional::get).collect(Collectors.toList());
    }

    @Override
    public User create(User user) {
        String sqlQuery = "insert into logins(login) " + "values " + "?";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sqlQuery, new String[]{"id"});
            stmt.setString(1, user.getLogin());
            return stmt;
        }, keyHolder);
        int id = keyHolder.getKey().intValue();

        String sqlQueryUsers = "insert into users(id, name, birthday) " + "values (?, ?, ?)";
        jdbcTemplate.update(sqlQueryUsers, id, user.getName(), user.getBirthday());

        String sqlQueryEmails = "insert into emails(id, email) " + "values (?, ?)";
        jdbcTemplate.update(sqlQueryEmails, id, user.getEmail());

        return get(id).get();
    }

    @Override
    public User change(User user) {
        Optional<User> userFrom = get(user.getId());

        if (userFrom.isEmpty()) return create(user);
        else {
            int id = user.getId();

            //Изменение пользователя
            String sqlQueryLogins = "update logins set " +
                    "login = ? " + "where id = ?";
            jdbcTemplate.update(sqlQueryLogins, user.getLogin(), id);

            String sqlQueryUsers = "update users set " +
                    "name = ? ," + "birthday = ? " + "where id = ?";
            jdbcTemplate.update(sqlQueryUsers, user.getName(), user.getBirthday(), id);

            String sqlQueryEmails = "update emails set " +
                    "email = ? " + "where id = ?";
            jdbcTemplate.update(sqlQueryEmails, user.getEmail(), id);

            return get(id).get();
        }
    }

    @Override
    public void delete(User user) {

    }
}
