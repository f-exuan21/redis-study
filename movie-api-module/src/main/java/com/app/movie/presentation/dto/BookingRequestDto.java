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

    public boolean isValidSeats() {
        if(seatIds == null || seatIds.isEmpty()) {
            return false;
        }

        for(int i = 1; i < seatIds.size(); i++) {
            if(seatIds.get(i) - seatIds.get(i - 1) != 1) {
                return false;
            }
        }

        long firstSeatId = seatIds.get(0);
        long blockStart = firstSeatId / 5 * 5 + 1;
        long blockEnd = blockStart + 4;

        for(long seat : seatIds) {
            if(seat < blockStart || seat > blockEnd) {
                return false;
            }
        }

        return true;
    }


}
