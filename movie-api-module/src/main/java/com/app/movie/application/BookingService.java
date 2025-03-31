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
import org.redisson.api.RLock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class BookingService {


    private final MessageSender messageSender;
    private final BookingTransactionalService bookingTransactionalService;

    @Autowired
    public BookingService(
                          MessageSender messageSender,
                          BookingTransactionalService bookingTransactionalService) {
        this.messageSender = messageSender;
        this.bookingTransactionalService = bookingTransactionalService;
    }

    public void bookShowtimeAndSendFcm(BookingRequestDto bookingRequestDto) {
        bookingTransactionalService.bookShowtime(bookingRequestDto);
        messageSender.send();
    }

}
