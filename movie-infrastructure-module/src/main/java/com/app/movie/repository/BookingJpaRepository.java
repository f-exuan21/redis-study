package com.app.movie.repository;

import com.app.movie.entity.BookingEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BookingJpaRepository extends JpaRepository<BookingEntity, Long> {

    boolean existsByShowtimeIdAndSeatId(Long showtimeId, Long seatId);

}
