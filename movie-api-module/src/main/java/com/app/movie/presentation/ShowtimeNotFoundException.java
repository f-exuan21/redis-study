package com.app.movie.presentation;

public class ShowtimeNotFoundException extends RuntimeException{

    private Long showtimeId;

    public ShowtimeNotFoundException(Long showtimeId) {
        this.showtimeId = showtimeId;
    }

    public Long getShowtimeId() {
        return showtimeId;
    }
}
