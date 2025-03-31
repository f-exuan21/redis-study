package com.app.movie.application;

import com.app.movie.aop.DistributedLock;
import com.app.movie.aop.DistributedLockExecutor;
import com.app.movie.model.Booking;
import com.app.movie.model.Seat;
import com.app.movie.model.Showtime;
import com.app.movie.presentation.DuplicateBookingException;
import com.app.movie.repository.BookingRepository;
import lombok.extern.slf4j.Slf4j;
import org.redisson.RedissonMultiLock;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class BookingLockService {

    private BookingRepository bookingRepository;

    private RedissonClient redissonClient;

    @Autowired
    public BookingLockService(BookingRepository bookingRepository, RedissonClient redissonClient) {
        this.bookingRepository = bookingRepository;
        this.redissonClient = redissonClient;
    }

    @DistributedLock(key = "'movie:' + #showtime.id + ':' + #seat.id")
    public void saveShowtime(Showtime showtime, Seat seat) {

        if(bookingRepository.isExists(showtime.getId(), seat.getId())) {
            throw new DuplicateBookingException();
        }

        Booking booking = new Booking();
        booking.setShowtime(showtime);
        booking.setSeat(seat);

        try {
            bookingRepository.save(booking);
        } catch (DataIntegrityViolationException e) {
            throw new DuplicateBookingException();
        }
    }

    public void saveShowtimeWithMultiLock(Long showtimeId, List<Long> seatIds) {

        List<RLock> locks = seatIds.stream()
                .map(seatId -> redissonClient.getLock("lock:showtime:" + showtimeId + ":seat:" + seatId))
                .toList();

        RLock multiLock = new RedissonMultiLock(locks.toArray(new RLock[0]));

        DistributedLockExecutor.withLock(multiLock, 0, 2, TimeUnit.SECONDS, () -> {
            seatIds.forEach(seatId -> {
                try {
                    Booking booking = Booking.book(showtimeId, seatId);
                    bookingRepository.save(booking);
                } catch(DataIntegrityViolationException e) {
                    throw new DuplicateBookingException();
                }
            });
        });

    }
}
