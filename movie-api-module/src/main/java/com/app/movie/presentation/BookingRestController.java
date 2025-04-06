package com.app.movie.presentation;

import com.app.movie.aop.ClientIpHolder;
import com.app.movie.application.BookingService;
import com.app.movie.application.RateLimiterRedisService;
import com.app.movie.presentation.dto.BookingRequestDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class BookingRestController {

    private final BookingService bookingService;

    private final RateLimiterRedisService rateLimiterRedisService;

    @Autowired
    public BookingRestController(BookingService bookingService, RateLimiterRedisService rateLimiterRedisService) {
        this.bookingService = bookingService;
        this.rateLimiterRedisService = rateLimiterRedisService;
    }

    @PostMapping("/book")
    public ResponseEntity<Void> bookMovie(@RequestBody BookingRequestDto bookingRequestDto) {
        bookingService.bookShowtimeAndSendFcm(bookingRequestDto);

        saveBookingInfoInRedis(bookingRequestDto);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    private void saveBookingInfoInRedis(BookingRequestDto bookingRequestDto) {
        String clientIp = ClientIpHolder.getClientIp();
        String key = "booking_limit:" + clientIp + ":" + bookingRequestDto.showtimeId();
        rateLimiterRedisService.setIfAbsent(key, 300);
    }

}
