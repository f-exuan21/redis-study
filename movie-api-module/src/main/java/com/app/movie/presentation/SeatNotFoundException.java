package com.app.movie.presentation;

public class SeatNotFoundException extends RuntimeException{

    private final Long seatId;

    public SeatNotFoundException(Long seatId) {
        this.seatId = seatId;
    }

    public Long getSeatId() {
        return seatId;
    }
}
