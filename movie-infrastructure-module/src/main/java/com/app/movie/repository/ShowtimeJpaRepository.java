package com.app.movie.repository;

import com.app.movie.entity.ShowtimeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

public interface ShowtimeJpaRepository extends JpaRepository<ShowtimeEntity, Long> {

    List<ShowtimeEntity> findByMovieId(Long movieId);
    List<ShowtimeEntity> findByMovie_ReleaseDateLessThanEqual(LocalDate date);

    @Query("SELECT s FROM ShowtimeEntity s " +
            "left join fetch s.theater " +
            "left join fetch s.movie " +
            "left join fetch s.movie.genre " +
            "WHERE s.movie.releaseDate <= :today " +
            "AND (:title IS NULL OR match(s.movie.title) against('KXT*' IN BOOLEAN MODE))" +
            "AND s.movie.genre.name IN :genres")
    List<ShowtimeEntity> findShowtimesByDateAndTitleAndGenre(@Param("today") LocalDate today,
                                                             @Param("title") String title,
                                                             @Param("genres") Set<String> genres);
}
