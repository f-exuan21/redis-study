package com.app.movie.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Booking {

    private Long id;
    private Showtime showtime;
    private Seat seat;

    public Booking(Showtime showtime, Seat seat) {
        this.showtime = showtime;
        this.seat = seat;
    }

    public static Booking book(Long showtimeId, Long seatId) {
        Showtime showtime = new Showtime(showtimeId);

        Seat seat = new Seat(seatId);

        Booking booking = new Booking(showtime, seat);
        return booking;
    }

}
