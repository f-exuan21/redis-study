package com.app.movie.presentation;

public class SeatNotFoundException extends RuntimeException{

    private final Long seatId;

    public SeatNotFoundException(Long seatId) {
        super("해당 seatID 는 존재하지 않음.");
        this.seatId = seatId;
    }

    public Long getSeatId() {
        return seatId;
    }
}
