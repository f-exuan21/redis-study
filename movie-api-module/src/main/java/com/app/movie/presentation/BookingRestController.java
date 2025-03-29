package com.app.movie.presentation;

import com.app.movie.application.BookingService;
import com.app.movie.presentation.dto.BookingRequestDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class BookingRestController {

    private final BookingService bookingService;

    @Autowired
    public BookingRestController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @PostMapping("/book")
    public ResponseEntity<Void> bookMovie(@RequestBody BookingRequestDto bookingRequestDto) {
        bookingService.bookShowtime(bookingRequestDto);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
