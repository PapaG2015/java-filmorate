package ru.yandex.practicum.filmorate.model;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.Setter;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.Duration;
import java.time.LocalDate;
import javax.validation.constraints.Positive;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@Builder
public class Film {
    private int id;
    @NotBlank
    private String name;
    @Size(max=200)
    private String description;
    private LocalDate releaseDate;
    @Positive
    private long duration;

    /*public void setDuration(long duration) {
        this.duration = Duration.ofSeconds(duration);
    }*/
}
