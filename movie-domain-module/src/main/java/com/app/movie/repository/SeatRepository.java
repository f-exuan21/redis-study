package com.app.movie.repository;

import com.app.movie.model.Seat;

import java.util.Optional;

public interface SeatRepository {

    Optional<Seat> findBySeatId(Long id);

}
