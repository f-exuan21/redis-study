package com.app.movie.repository;

import com.app.movie.model.Showtime;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

public interface ShowtimeRepository{

    List<Showtime> findShowtimesByDateAndTitleAndGenre(LocalDate localDate, String title, Set<String> genres);

}
