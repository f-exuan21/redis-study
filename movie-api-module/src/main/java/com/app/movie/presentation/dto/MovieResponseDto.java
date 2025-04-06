package com.app.movie.presentation.dto;

import com.app.movie.model.Movie;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

@Getter
public class MovieResponseDto {

    private Long id;
    private String title;
    private String thumbnail;
    private String rating;
    private int runningTime;
    private String genre;
    private LocalDate releaseDate;
    private List<TheaterShowtime> theaterShowtimes;

    public MovieResponseDto(Long id,
                            String title,
                            String thumbnail,
                            String rating,
                            int runningTime,
                            String genre,
                            LocalDate releaseDate,
                            List<TheaterShowtime> theaterShowtimes) {
        this.id = id;
        this.title = title;
        this.thumbnail = thumbnail;
        this.rating = rating;
        this.runningTime = runningTime;
        this.genre = genre;
        this.releaseDate = releaseDate;
        this.theaterShowtimes = theaterShowtimes;
    }


}
