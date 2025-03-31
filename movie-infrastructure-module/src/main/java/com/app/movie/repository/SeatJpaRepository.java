package com.app.movie.repository;

import com.app.movie.entity.SeatEntity;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;

import java.util.Optional;

public interface SeatJpaRepository extends JpaRepository<SeatEntity, Long> {

//    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<SeatEntity> findById(Long id);

}
