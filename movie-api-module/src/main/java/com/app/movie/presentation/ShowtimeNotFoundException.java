package com.app.movie.presentation;

public class ShowtimeNotFoundException extends RuntimeException{

    private Long showtimeId;

    public ShowtimeNotFoundException(Long showtimeId) {
        super("해당 showtimeID 는 존재하지 않음");
        this.showtimeId = showtimeId;
    }

    public Long getShowtimeId() {
        return showtimeId;
    }
}
