package com.app.movie.application;

import com.app.movie.model.Movie;
import com.app.movie.model.Showtime;
import com.app.movie.model.Theater;
import com.app.movie.presentation.dto.MovieRequestDto;
import com.app.movie.presentation.dto.MovieResponseDto;
import com.app.movie.presentation.dto.TheaterShowtime;
import com.app.movie.repository.MovieRepository;
import com.app.movie.repository.ShowtimeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class MovieService {

    MovieRepository movieRepository;
    ShowtimeRepository showtimeRepository;

    @Autowired
    public MovieService(MovieRepository movieRepository, ShowtimeRepository showtimeRepository) {
        this.movieRepository = movieRepository;
        this.showtimeRepository = showtimeRepository;
    }


    public List<MovieResponseDto> getAllMoviesBytitle(MovieRequestDto movieRequestDto) {
        LocalDate today = LocalDate.now();
        List<Showtime> showtimes = showtimeRepository.findShowtimesByDateAndTitleAndGenre(
                today,
                movieRequestDto.getTitle(),
                movieRequestDto.getGenres()
        );

        List<MovieResponseDto> movieResponseDtoList = showtimes.stream()
                .collect(Collectors.groupingBy(Showtime::getMovie))
                .entrySet().stream()
                .map(entry -> {
                            Movie movie = entry.getKey();
                            List<Showtime> movieShowtime = entry.getValue();

                            Map<Theater, List<LocalTime>> groupedByTheater = movieShowtime.stream()
                                    .collect(Collectors.groupingBy(
                                            Showtime::getTheater,
                                            Collectors.mapping(Showtime::getStartTime, Collectors.toList())
                                    ));

                            List<TheaterShowtime> theaterShowtime = groupedByTheater.entrySet().stream()
                                    .map(e -> new TheaterShowtime(
                                            e.getKey().getId(),
                                            e.getKey().getName(),
                                            e.getValue().stream()
                                                    .sorted()
                                                    .collect(Collectors.toList()),
                                            movie.getDuration()))
                                    .toList();


                            MovieResponseDto dto = new MovieResponseDto(
                                    movie.getId(),
                                    movie.getTitle(),
                                    movie.getThumbnailURL(),
                                    movie.getRating().getDescription(),
                                    movie.getDuration(),
                                    movie.getGenre().getName(),
                                    movie.getReleaseDate()
                            );

                            dto.setTheaterShowtimes(theaterShowtime);
                            return dto;
                        }
                )
                .sorted(Comparator.comparing(MovieResponseDto::getReleaseDate).reversed())
                .collect(Collectors.toList());

        return movieResponseDtoList;
    }

}
