package com.app.movie.repository;

import com.app.movie.model.Booking;

public interface BookingRepository {

    void save(Booking booking);

    boolean isExists(Long showtimeId, Long seatId);

    long countByShowtimeIdAndSeatId(Long showtimeId, Long seatId);

}
