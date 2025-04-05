package com.app.movie.aop;

import com.app.movie.presentation.BookingFrequencyLimitException;
import com.app.movie.presentation.dto.BookingRequestDto;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Aspect
@Component
public class BookingRateLimitCheckAspect {

    private final HttpServletRequest request;

    private final Cache<String, Boolean> bookingAttemptsCache = CacheBuilder.newBuilder()
            .expireAfterWrite(5, TimeUnit.MINUTES)
            .build();

    public BookingRateLimitCheckAspect(HttpServletRequest request) {
        this.request = request;
    }

    @Before("@annotation(BookingRateLimitCheck) && args(bookingRequestDto,..)")
    public void checkBookingRateLimit(BookingRequestDto bookingRequestDto) {

        String clientIp = HttpHeaders.getClientIp(request);
        Long showtimeId = bookingRequestDto.showtimeId();
        String key = clientIp + ":" + showtimeId;

        if (bookingAttemptsCache.getIfPresent(key) != null) {
            throw new BookingFrequencyLimitException();
        }

        bookingAttemptsCache.put(key, Boolean.TRUE);

    }

}
