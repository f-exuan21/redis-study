package com.app.movie.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

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

    public static boolean isValidSeats(List<Long> seatIds) {
        if(seatIds == null || seatIds.isEmpty()) {
            return false;
        }

        for(int i = 1; i < seatIds.size(); i++) {
            if(seatIds.get(i) - seatIds.get(i - 1) != 1) {
                return false;
            }
        }

        long firstSeatId = seatIds.get(0);
        long blockStart = firstSeatId / 6 * 5 + 1;
        long blockEnd = blockStart + 4;

        for(long seat : seatIds) {
            if(seat < blockStart || seat > blockEnd) {
                return false;
            }
        }

        return true;
    }

}
