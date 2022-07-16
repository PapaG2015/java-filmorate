package ru.yandex.filmorate.model;

import lombok.Data;

import javax.validation.constraints.Positive;

@Data
public class Genre {
    private int id;
    private String name;

    public Genre(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public Genre() {

    }
}




