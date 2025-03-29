package com.app.movie.adapter;

import com.app.movie.mapper.SeatMapper;
import com.app.movie.model.Seat;
import com.app.movie.repository.SeatJpaRepository;
import com.app.movie.repository.SeatRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class SeatRepositoryAdapter implements SeatRepository {

    private final SeatJpaRepository seatJpaRepository;

    @Autowired
    public SeatRepositoryAdapter(SeatJpaRepository seatJpaRepository) {
        this.seatJpaRepository = seatJpaRepository;
    }

    @Override
    public Optional<Seat> findBySeatId(Long id) {
        Optional<Seat> seat = seatJpaRepository.findById(id)
                .map(SeatMapper::toDomain);
        return seat;
    }
}
