package com.app.movie.application;

import com.app.movie.model.Booking;
import com.app.movie.presentation.InvalidSeatSelectionException;
import com.app.movie.presentation.SeatNotFoundException;
import com.app.movie.presentation.ShowtimeNotFoundException;
import com.app.movie.presentation.dto.BookingRequestDto;
import com.app.movie.repository.SeatRepository;
import com.app.movie.repository.ShowtimeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class BookingTransactionalService {

    private final BookingLockService bookingLockService;
    private final ShowtimeRepository showtimeRepository;
    private final SeatRepository seatRepository;

    @Autowired
    public BookingTransactionalService(BookingLockService bookingLockService,
                                       ShowtimeRepository showtimeRepository,
                                       SeatRepository seatRepository) {
        this.bookingLockService = bookingLockService;
        this.showtimeRepository = showtimeRepository;
        this.seatRepository = seatRepository;
    }

    @Transactional
    public void bookShowtime(BookingRequestDto bookingRequestDto){

        if(!Booking.isValidSeats(bookingRequestDto.seatIds())) {
            throw new InvalidSeatSelectionException();
        }

        Long showtimeId = bookingRequestDto.showtimeId();
        List<Long> seatIds = bookingRequestDto.seatIds();

        showtimeRepository.findByShowtimeId(bookingRequestDto.showtimeId())
                .orElseThrow(() -> new ShowtimeNotFoundException(showtimeId));

        seatIds.forEach(seatId -> {
            seatRepository.findBySeatId(seatId)
                    .orElseThrow(() -> new SeatNotFoundException(seatId));
        });

        bookingLockService.saveShowtimeWithMultiLock(showtimeId, seatIds);

    }

}
