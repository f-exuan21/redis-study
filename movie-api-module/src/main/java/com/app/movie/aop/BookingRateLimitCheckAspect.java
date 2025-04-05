package com.app.movie.aop;

import com.app.movie.application.RateLimiterRedisService;
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
    private final RateLimiterRedisService rateLimiterRedisService;

    public BookingRateLimitCheckAspect(HttpServletRequest request, RateLimiterRedisService rateLimiterRedisService) {
        this.request = request;
        this.rateLimiterRedisService = rateLimiterRedisService;
    }

    @Before("@annotation(BookingRateLimitCheck) && args(bookingRequestDto,..)")
    public void checkBookingRateLimit(BookingRequestDto bookingRequestDto) {

        String clientIp = HttpHeaders.getClientIp(request);
        String key = "booking_limit:" + clientIp + ":" + bookingRequestDto.showtimeId();
        rateLimiterRedisService.setIfAbsent(key, 300);

    }

}
