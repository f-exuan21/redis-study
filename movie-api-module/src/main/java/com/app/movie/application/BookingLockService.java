package com.app.movie.application;

import com.app.movie.aop.DistributedLock;
import com.app.movie.model.Booking;
import com.app.movie.model.Seat;
import com.app.movie.model.Showtime;
import com.app.movie.presentation.DuplicateBookingException;
import com.app.movie.repository.BookingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BookingLockService {

    private BookingRepository bookingRepository;

    @Autowired
    public BookingLockService(BookingRepository bookingRepository) {
        this.bookingRepository = bookingRepository;
    }

    @DistributedLock(key = "'movie:' + #showtime.id + ':' + #seat.id")
    public void saveShowtime(Showtime showtime, Seat seat) {

        if(bookingRepository.isExists(showtime.getId(), seat.getId())) {
            throw new DuplicateBookingException();
        }

        Booking booking = new Booking();
        booking.setShowtime(showtime);
        booking.setSeat(seat);

        bookingRepository.save(booking);
    }
}
