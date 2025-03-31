package com.app.movie.repository;

import com.app.movie.entity.ShowtimeEntity;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface ShowtimeJpaRepository extends JpaRepository<ShowtimeEntity, Long> {

//    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<ShowtimeEntity> findById(Long movieId);
    List<ShowtimeEntity> findByMovie_ReleaseDateLessThanEqual(LocalDate date);

    @Query("SELECT s FROM ShowtimeEntity s " +
            "left join fetch s.theater " +
            "left join fetch s.movie " +
            "left join fetch s.movie.genre " +
            "WHERE s.movie.releaseDate <= :today " +
            "AND (:title IS NULL OR s.movie.title LIKE CONCAT('%', :title, '%'))" +
            "AND s.movie.genre.name IN :genres")
    List<ShowtimeEntity> findShowtimesByDateAndTitleAndGenre(@Param("today") LocalDate today,
                                                             @Param("title") String title,
                                                             @Param("genres") Set<String> genres);
}
