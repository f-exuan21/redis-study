package com.app.movie.application;

import com.app.movie.presentation.dto.BookingRequestDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BookingService {


    private final MessageSender messageSender;
    private final BookingTransactionalService bookingTransactionalService;

    @Autowired
    public BookingService(
                          MessageSender messageSender,
                          BookingTransactionalService bookingTransactionalService,
                          RateLimiterRedisService rateLimiterRedisService) {
        this.messageSender = messageSender;
        this.bookingTransactionalService = bookingTransactionalService;
    }

    public void bookShowtimeAndSendFcm(BookingRequestDto bookingRequestDto) {
        bookingTransactionalService.bookShowtime(bookingRequestDto);
        messageSender.send();
    }

}
