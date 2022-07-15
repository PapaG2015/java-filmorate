package ru.yandex.filmorate.model;

import lombok.Data;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.validation.constraints.Positive;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data //позволяет генерировать геттеры, сеттеры, методы toString(), equals() и hashCode()
public class Film {

    private int id;
    @NotBlank
    private String name;
    @Size(max = 200)
    private String description;
    private LocalDate releaseDate;
    @Positive
    private long duration;
    // private Set<Integer> likes = new HashSet<>();
    //private String rating;
    //private Set<Integer> genres = new HashSet<>();
    private Set<Genre> genres = new HashSet<>();
    private int rate;
    private MPA mpa;

    public Film(int id, @NotBlank String name, @Size(max = 200) String description, LocalDate releaseDate, @Positive long duration, MPA mpa) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
        this.mpa = mpa;
    }
}
