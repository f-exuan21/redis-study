package com.app.movie.presentation.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Objects;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MovieRequestDto {

    @Size(max=10)
    private String title;
    private Set<String> genres;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MovieRequestDto that = (MovieRequestDto) o;
        return Objects.equals(title, that.title) && Objects.equals(genres, that.genres);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, genres);
    }

    @Override
    public String toString() {
        return "MovieRequestDto{" +
                "title='" + title + '\'' +
                ", genres=" + genres +
                '}';
    }
}
