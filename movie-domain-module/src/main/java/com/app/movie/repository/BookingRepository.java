package com.app.movie.repository;

import com.app.movie.model.Booking;
import com.app.movie.model.Seat;
import com.app.movie.model.Showtime;

public interface BookingRepository {

    void save(Booking booking);

    boolean isExists(Long showtimeId, Long seatId);

    long countByShowtimeIdAndSeatId(Long showtimeId, Long seatId);

    void delete(Booking booking);

    Booking findByShowtimeIdAndSeatId(Long showtimeId, Long seatId);

}
