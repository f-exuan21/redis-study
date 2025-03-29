package com.app.movie.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Showtime")
@Getter
@Setter
public class ShowtimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "theater_id")
    private TheaterEntity theater;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "movie_id")
    private MovieEntity movie;
    private LocalTime startTime;
    private LocalTime endTime;

    private String createdBy;
    private LocalDateTime createdAt;
    private String updatedBy;
    private LocalDateTime updatedAt;

    public ShowtimeEntity(Long id) {
        this.id = id;
    }

    public MovieEntity getMovie() {
        return movie;
    }

    public void setMovie(MovieEntity movie) {
        this.movie = movie;
    }

    public TheaterEntity getTheater() {
        return theater;
    }

    public void setTheater(TheaterEntity theater) {
        this.theater = theater;
    }



}
