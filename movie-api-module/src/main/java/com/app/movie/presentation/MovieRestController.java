package com.app.movie.presentation;

import com.app.movie.aop.RateLimitCheck;
import com.app.movie.application.MovieService;
import com.app.movie.presentation.dto.MovieRequestDto;
import com.app.movie.presentation.dto.MovieResponseDto;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/movie")
public class MovieRestController {

    private final MovieService movieService;

    @Autowired
    public MovieRestController(MovieService movieService) {
        this.movieService = movieService;
    }

    @GetMapping()
    @RateLimitCheck
    public List<MovieResponseDto> getMovies(@RequestHeader(value = "X-Forwarded-For", required = false) String ipAddress,
                                            @Valid @ModelAttribute MovieRequestDto movieRequestDto) {

        return movieService.getAllMovies(movieRequestDto);
    }


}
