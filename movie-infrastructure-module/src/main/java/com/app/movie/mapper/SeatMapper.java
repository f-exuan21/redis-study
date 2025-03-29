package com.app.movie.mapper;


import com.app.movie.entity.SeatEntity;
import com.app.movie.model.Seat;

import java.time.LocalDateTime;

public class SeatMapper {

    public static Seat toDomain(SeatEntity entity) {
        return new Seat(entity.getId(), entity.getName());
    }

    public static SeatEntity toEntity(Seat seat) {
        return new SeatEntity(seat.getId(), seat.getName());
    }


}
