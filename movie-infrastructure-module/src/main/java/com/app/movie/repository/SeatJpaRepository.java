package com.app.movie.repository;

import com.app.movie.entity.SeatEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SeatJpaRepository extends JpaRepository<SeatEntity, Long> {
}
