package com.app.movie.adapter;

import com.app.movie.entity.BookingEntity;
import com.app.movie.entity.SeatEntity;
import com.app.movie.entity.ShowtimeEntity;
import com.app.movie.mapper.SeatMapper;
import com.app.movie.mapper.ShowtimeMapper;
import com.app.movie.model.Booking;
import com.app.movie.model.Seat;
import com.app.movie.model.Showtime;
import com.app.movie.repository.BookingJpaRepository;
import com.app.movie.repository.BookingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.awt.print.Book;
import java.util.Optional;

@Repository
public class BookingRepositoryAdapter implements BookingRepository {

    private final BookingJpaRepository bookingJpaRepository;

    @Autowired
    public BookingRepositoryAdapter(BookingJpaRepository bookingJpaRepository) {
        this.bookingJpaRepository = bookingJpaRepository;
    }

    @Override
    public void save(Booking booking) {
        BookingEntity bookingEntity = BookingEntity.of(booking);
        bookingJpaRepository.save(bookingEntity);
    }

    @Override
    public boolean isExists(Long showtimeId, Long seatId) {
        return bookingJpaRepository.existsByShowtimeIdAndSeatId(showtimeId, seatId);
    }

    @Override
    public long countByShowtimeIdAndSeatId(Long showtimeId, Long seatId) {
        return bookingJpaRepository.countByShowtimeIdAndSeatId(showtimeId, seatId);
    }

    @Override
    public void delete(Booking booking) {
        bookingJpaRepository.findById(booking.getId())
                        .ifPresent(bookingJpaRepository::delete);
    }

    @Override
    public Booking findByShowtimeIdAndSeatId(Long showtimeId, Long seatId) {
        Optional<BookingEntity> bookingEntity = bookingJpaRepository.findByShowtime_IdAndSeat_Id(
                showtimeId,
                seatId
        );
        return bookingEntity.map(BookingEntity::toBooking).orElse(null);
    }
}
