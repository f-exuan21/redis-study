package com.app.movie.repository;

import com.app.movie.entity.BookingEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookingJpaRepository extends JpaRepository<BookingEntity, Long> {
}
