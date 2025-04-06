package com.app.movie.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class Seat {

    private Long id;
    private String name;

    public Seat(Long id) {
        this.id = id;
    }
}
