package com.app.movie.presentation.dto;


import java.util.List;

public record BookingRequestDto(Long showtimeId, List<Long> seatIds) {

    public BookingRequestDto {
        if (seatIds != null) {
            seatIds = seatIds.stream()
                    .sorted()
                    .toList();
        }
    }

}
