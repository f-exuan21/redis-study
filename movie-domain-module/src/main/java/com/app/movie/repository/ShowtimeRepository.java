package com.app.movie.repository;

import com.app.movie.model.Showtime;

import java.time.LocalDate;
import java.util.List;

public interface ShowtimeRepository{

    List<Showtime> findShowtimesByDateAndTitleAndGenre(LocalDate localDate, String title, List<String> genres);

}
