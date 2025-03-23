package com.app.movie.application;


import com.app.movie.presentation.dto.MovieRequestDto;
import com.app.movie.presentation.dto.MovieResponseDto;
import com.app.movie.presentation.dto.TheaterShowtime;
import com.app.movie.repository.ShowtimeRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import com.app.movie.model.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MovieServiceTest {

    @InjectMocks
    private MovieService movieService; // 테스트할 서비스 클래스

    @Mock
    private ShowtimeRepository showtimeRepository; // 서비스가 의존하는 repository

    @Test
    public void findAllMovies() {
        // given: 테스트에 사용할 더미 데이터 생성

        // Theater 데이터
        Theater theater = new Theater();
        theater.setId(1L);
        theater.setName("Theater One");

        // Genre 데이터 (필요에 따라)
        Genre genre = new Genre();
        genre.setName("Action");

        // Movie 데이터
        Movie movie = new Movie();
        movie.setId(100L);
        movie.setTitle("Test Movie");
        movie.setRating(Rating.G);
        movie.setDuration(120);
        movie.setGenre(genre);
        movie.setReleaseDate(LocalDate.of(2023, 1, 1)); // 오늘 포함 이전 날짜

        // Showtime 데이터 - 하나의 영화에 대해 두 개의 상영시간
        Showtime showtime1 = new Showtime();
        showtime1.setId(10L);
        showtime1.setTheater(theater);
        showtime1.setMovie(movie);
        showtime1.setStartTime(LocalTime.of(14, 0));

        Showtime showtime2 = new Showtime();
        showtime2.setId(11L);
        showtime2.setTheater(theater);
        showtime2.setMovie(movie);
        showtime2.setStartTime(LocalTime.of(16, 0));

        List<Showtime> showtimes = Arrays.asList(showtime1, showtime2);
        LocalDate today = LocalDate.now();


        when(showtimeRepository.findShowtimesByDateAndTitleAndGenre(any(LocalDate.class), eq(null), eq(null)))
                .thenReturn(showtimes);

        // when: 실제 서비스 메서드 호출
        MovieRequestDto movieRequestDto = new MovieRequestDto();

        List<MovieResponseDto> result = movieService.getAllMoviesByTitle(movieRequestDto);
        System.out.println("===========");
        result.get(0).getTheaterShowtimes().forEach(showtime-> {
            System.out.println(showtime.getStartTime());
        });
        System.out.println("===========");

        // then: 결과 검증
        assertNotNull(result);


        MovieResponseDto dto = result.get(0);
        assertNotNull(dto);
        assertEquals(movie.getId(), dto.getId());
        assertEquals(movie.getTitle(), dto.getTitle());
        assertEquals(movie.getRating().getDescription(), dto.getRating());
        assertEquals(movie.getDuration(), dto.getRunningTime());
        assertEquals(genre.getName(), dto.getGenre());

        List<TheaterShowtime> theaterShowtimes = dto.getTheaterShowtimes();
        assertNotNull(theaterShowtimes);
        // 동일 Theater에 두 개의 상영시간이 존재하므로 그룹핑 되어 하나의 TheaterShowtime 객체 생성됨
        assertEquals(1, theaterShowtimes.size());

        TheaterShowtime ts = theaterShowtimes.get(0);
        assertEquals(theater.getId(), ts.getTheaterId());
        assertEquals(theater.getName(), ts.getTheaterName());
        // 두 개의 상영시간이 들어 있어야 함
        assertEquals(2, ts.getStartTime().size());
        assertTrue(ts.getStartTime().contains(showtime1.getStartTime()));
        assertTrue(ts.getStartTime().contains(showtime2.getStartTime()));
    }

}