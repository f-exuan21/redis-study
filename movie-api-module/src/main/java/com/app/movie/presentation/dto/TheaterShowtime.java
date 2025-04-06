package com.app.movie.presentation.dto;

import lombok.Getter;

import java.time.LocalTime;
import java.util.List;

@Getter
public class TheaterShowtime {

    private Long theaterId;
    private String theaterName;
    private List<LocalTime> startTime;

    public TheaterShowtime(Long theaterId, String theaterName, List<LocalTime> startTime, int runningTime) {
        this.theaterId = theaterId;
        this.theaterName = theaterName;
        this.startTime = startTime;
    }

}
