package ru.yandex.filmorate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.filmorate.model.Film;
import ru.yandex.filmorate.model.MPA;
import ru.yandex.filmorate.model.User;
import ru.yandex.filmorate.storage.film.FilmStorage;
import ru.yandex.filmorate.storage.user.UserStorage;

import java.time.LocalDate;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class FilmorateApplicationTests {
    @Autowired
    private UserStorage userDbStorage;
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private FilmStorage filmDbStorage;

    @BeforeEach
    public void cleanDB() {
        String str = "DELETE FROM logins WHERE id = ?";
        jdbcTemplate.update(str, 1);
    }

    @Test
    public void testAddUser() {

        User user = new User(2,"Marat", LocalDate.of(1987,11,18));
        user.setEmail("karimovmar@yandex.ru");
        user.setLogin("PapaG");

        userDbStorage.create(user);

        assertEquals(1, userDbStorage.get(1).get().getId());
    }

    @Test
    public void testAddUser2() {

        User user = new User(10,"Bob", LocalDate.of(2000,10,11));
        user.setEmail("boo@yandex.ru");
        user.setLogin("be");

        userDbStorage.create(user);

        assertEquals("Bob", userDbStorage.get(2).get().getName());

        String sql = "select * from emails where email = ?";
        Collection<Integer> list = jdbcTemplate.query(sql, (rs, rowNum) -> rs.getInt("id"), "karimovmar@yandex.ru");

        String sql2 = "select * from emails where email = ?";
        Collection<Integer> list2 = jdbcTemplate.query(sql2, (rs, rowNum) -> rs.getInt("id"), "boo@yandex.ru");
    }

    @Test
    public void testAddFilm() {

        Film film = new Film(10, "qqq", "desc", LocalDate.of(1900,10,10),10, new MPA(1, "2323"));

        filmDbStorage.create(film);

        assertEquals("qqq", filmDbStorage.get(1).get().getName());
    }
}
