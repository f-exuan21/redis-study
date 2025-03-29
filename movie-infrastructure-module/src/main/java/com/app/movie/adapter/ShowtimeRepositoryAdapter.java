package com.app.movie.adapter;

import com.app.movie.entity.GenreEntity;
import com.app.movie.entity.MovieEntity;
import com.app.movie.entity.ShowtimeEntity;
import com.app.movie.entity.TheaterEntity;
import com.app.movie.mapper.ShowtimeMapper;
import com.app.movie.model.Genre;
import com.app.movie.model.Movie;
import com.app.movie.model.Showtime;
import com.app.movie.model.Theater;
import com.app.movie.repository.ShowtimeJpaRepository;
import com.app.movie.repository.ShowtimeRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public class ShowtimeRepositoryAdapter implements ShowtimeRepository {

    ShowtimeJpaRepository showtimeJpaRepository;

    public ShowtimeRepositoryAdapter(ShowtimeJpaRepository showtimeJpaRepository) {
        this.showtimeJpaRepository = showtimeJpaRepository;
    }


    @Override
    public List<Showtime> findShowtimesByDateAndTitleAndGenre(LocalDate localDate, String title, Set<String> genres) {
        List<ShowtimeEntity> showtimeEntities = showtimeJpaRepository
                .findShowtimesByDateAndTitleAndGenre(
                        localDate,
                        title,
                        genres);
        List<Showtime> showtimes = showtimeEntities.stream().map(ShowtimeMapper::convertToDomain).toList();
        return showtimes;
    }

    @Override
    public Optional<Showtime> findByShowtimeId(Long id) {
        Optional<Showtime> showtime = showtimeJpaRepository.findById(id)
                .map(ShowtimeMapper::convertToDomainWithoutTheaterAndMovie);

        return showtime;
    }

}
