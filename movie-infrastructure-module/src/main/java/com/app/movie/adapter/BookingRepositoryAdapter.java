package com.app.movie.adapter;

import com.app.movie.entity.BookingEntity;
import com.app.movie.entity.SeatEntity;
import com.app.movie.entity.ShowtimeEntity;
import com.app.movie.model.Booking;
import com.app.movie.repository.BookingJpaRepository;
import com.app.movie.repository.BookingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.awt.print.Book;

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
        System.out.println(bookingEntity);
        bookingJpaRepository.save(bookingEntity);
    }
}
