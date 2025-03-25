package com.app.movie.presentation.dto;

import jakarta.validation.constraints.Size;

import java.util.Set;

public record MovieRequestDto(String title, Set<String> genres) {

    public MovieRequestDto() {
        this(null, null);
    }

}
