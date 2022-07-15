package ru.yandex.filmorate.model;

import lombok.Data;

import javax.validation.constraints.Positive;

@Data
public class MPA {
    private int id;
    private String name;

    public MPA(int id, String str) {
        this.id = id;
        this.name = str;
    }

    public MPA() {

    }
}
