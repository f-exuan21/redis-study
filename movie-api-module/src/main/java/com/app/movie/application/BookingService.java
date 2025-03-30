package com.app.movie.application;

import com.app.movie.aop.DistributedLock;
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

    private final BookingLockService bookingLockService;
    private final ShowtimeRepository showtimeRepository;
    private final SeatRepository seatRepository;
    private final MessageSender messageSender;

    @Autowired
    public BookingService(BookingLockService bookingLockService, ShowtimeRepository showtimeRepository, SeatRepository seatRepository, MessageSender messageSender) {
        this.bookingLockService = bookingLockService;
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

        Showtime foundShowtime = showtimeRepository.findByShowtimeId(bookingRequestDto.showtimeId())
                .orElseThrow(() -> new ShowtimeNotFoundException(showtimeId));


        seatIds.forEach(seatId -> {
            Seat foundSeat = seatRepository.findBySeatId(seatId)
                    .orElseThrow(() -> new SeatNotFoundException(seatId));

            bookingLockService.saveShowtime(foundShowtime, foundSeat);

        });


        messageSender.send();

    }



}
