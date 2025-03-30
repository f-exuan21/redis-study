package com.app.movie.application;

import com.app.movie.model.Booking;
import com.app.movie.model.Seat;
import com.app.movie.model.Showtime;
import com.app.movie.presentation.DuplicateBookingException;
import com.app.movie.presentation.InvalidSeatSelectionException;
import com.app.movie.presentation.SeatNotFoundException;
import com.app.movie.presentation.ShowtimeNotFoundException;
import com.app.movie.presentation.dto.BookingRequestDto;
import com.app.movie.repository.BookingRepository;
import com.app.movie.repository.SeatRepository;
import com.app.movie.repository.ShowtimeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class BookingService {

    private final BookingRepository bookingRepository;
    private final ShowtimeRepository showtimeRepository;
    private final SeatRepository seatRepository;
    private final MessageSender messageSender;

    @Autowired
    public BookingService(BookingRepository bookingRepository, ShowtimeRepository showtimeRepository, SeatRepository seatRepository, MessageSender messageSender) {
        this.bookingRepository = bookingRepository;
        this.showtimeRepository = showtimeRepository;
        this.seatRepository = seatRepository;
        this.messageSender = messageSender;
    }

    @Transactional
    public void bookShowtime(BookingRequestDto bookingRequestDto){

        if(!bookingRequestDto.isValidSeats()) {
            throw new InvalidSeatSelectionException();
        }

        Long showtimeId = bookingRequestDto.showtimeId();
        List<Long> seatIds = bookingRequestDto.seatIds();

        Showtime showtime = showtimeRepository.findByShowtimeId(bookingRequestDto.showtimeId())
                .orElseThrow(() -> new ShowtimeNotFoundException(showtimeId));


        seatIds.forEach(seatId -> {

            Seat seat = seatRepository.findBySeatId(seatId)
                    .orElseThrow(() -> new SeatNotFoundException(seatId));

            if(bookingRepository.isExists(showtimeId, seat.getId())) {
                throw new DuplicateBookingException();
            }

            Booking booking = new Booking();
            booking.setShowtime(showtime);
            booking.setSeat(seat);

            bookingRepository.save(booking);

        });


        messageSender.send();

    }

}
