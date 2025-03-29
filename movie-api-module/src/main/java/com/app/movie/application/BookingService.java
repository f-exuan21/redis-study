package com.app.movie.application;

import com.app.movie.model.Booking;
import com.app.movie.model.Movie;
import com.app.movie.model.Seat;
import com.app.movie.model.Showtime;
import com.app.movie.presentation.SeatNotFoundException;
import com.app.movie.presentation.ShowtimeNotFoundException;
import com.app.movie.presentation.dto.BookingRequestDto;
import com.app.movie.repository.BookingRepository;
import com.app.movie.repository.SeatRepository;
import com.app.movie.repository.ShowtimeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BookingService {

    private final BookingRepository bookingRepository;
    private final ShowtimeRepository showtimeRepository;
    private final SeatRepository seatRepository;

    @Autowired
    public BookingService(BookingRepository bookingRepository, ShowtimeRepository showtimeRepository, SeatRepository seatRepository) {
        this.bookingRepository = bookingRepository;
        this.showtimeRepository = showtimeRepository;
        this.seatRepository = seatRepository;
    }

    @Transactional
    public void bookShowtime(BookingRequestDto bookingRequestDto) {

        Showtime showtime = showtimeRepository.findByShowtimeId(bookingRequestDto.showtimeId())
                .orElseThrow(() -> new ShowtimeNotFoundException(bookingRequestDto.showtimeId()));

        Seat seat = seatRepository.findBySeatId(bookingRequestDto.seatId())
                .orElseThrow(() -> new SeatNotFoundException(bookingRequestDto.seatId()));

        Booking booking = new Booking();
        booking.setShowtime(showtime);
        booking.setSeat(seat);

        bookingRepository.save(booking);

    }

}
