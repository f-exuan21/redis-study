package com.app.movie.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalTime;

@NoArgsConstructor
@Getter
public class Showtime {

    private Long id;
    private Theater theater;
    private Movie movie;
    private LocalTime startTime;
    private LocalTime endTime;

    public Showtime(Long id) {
        this.id = id;
        setEndTime();
    }

    public Showtime(Long id, Theater theater, Movie movie, LocalTime startTime) {
        this.id = id;
        this.theater = theater;
        this.movie = movie;
        this.startTime = startTime;
        setEndTime();
    }

    private void setEndTime() {
        if (movie != null) {
            this.endTime = startTime.plusMinutes(movie.getDuration());
        }
    }

}
