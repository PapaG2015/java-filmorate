package ru.yandex.practicum.filmorate.model;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.Duration;
import java.time.LocalDate;

@Data
@Builder
public class Film {
    private int id;
    private String name;
    @NotNull
    private String description;
    private LocalDate releaseDate;
    private long duration;

    /*public void setDuration(long duration) {
        this.duration = Duration.ofSeconds(duration);
    }*/
}
