package com.app.movie.repository;

import com.app.movie.entity.BookingEntity;
import com.app.movie.entity.SeatEntity;
import com.app.movie.entity.ShowtimeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BookingJpaRepository extends JpaRepository<BookingEntity, Long> {

    boolean existsByShowtimeIdAndSeatId(Long showtimeId, Long seatId);

    long countByShowtimeIdAndSeatId(Long showtimeId, Long seatId);

    Optional<BookingEntity> findByShowtime_IdAndSeat_Id(Long showtimeId, Long seatId);
}
