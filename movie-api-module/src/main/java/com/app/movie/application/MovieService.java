package com.app.movie.application;

import com.app.movie.aop.DistributedLock;
import com.app.movie.model.Movie;
import com.app.movie.model.Showtime;
import com.app.movie.model.Theater;
import com.app.movie.presentation.dto.MovieRequestDto;
import com.app.movie.presentation.dto.MovieResponseDto;
import com.app.movie.presentation.dto.TheaterShowtime;
import com.app.movie.repository.MovieRepository;
import com.app.movie.repository.ShowtimeRepository;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class MovieService {

    private final ShowtimeRepository showtimeRepository;


    @Autowired
    public MovieService(ShowtimeRepository showtimeRepository) {
        this.showtimeRepository = showtimeRepository;
    }


    @Cacheable(value = "movieCache", key = "#movieRequestDto", sync = true)
    public List<MovieResponseDto> getAllMovies(MovieRequestDto movieRequestDto) {
        return loadAllMovies(movieRequestDto);
    }

    public List<MovieResponseDto> loadAllMovies(MovieRequestDto movieRequestDto) {
        LocalDate today = LocalDate.now();
        List<Showtime> showtimes = showtimeRepository.findShowtimesByDateAndTitleAndGenre(
                today,
                movieRequestDto.title(),
                movieRequestDto.genres()
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
