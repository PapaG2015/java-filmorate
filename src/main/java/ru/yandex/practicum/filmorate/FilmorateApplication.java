package ru.yandex.practicum.filmorate;
import com.google.gson.Gson;
import ru.yandex.practicum.filmorate.model.Film;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.time.Duration;
import java.time.LocalDate;

@SpringBootApplication
public class FilmorateApplication {

	public static void main(String[] args) {
		SpringApplication.run(FilmorateApplication.class, args);

	}

}
