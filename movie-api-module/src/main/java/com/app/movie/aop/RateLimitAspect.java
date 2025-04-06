package com.app.movie.aop;

import com.app.movie.application.RateLimiterRedisService;
import com.app.movie.presentation.TooManyRequestException;
import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;


@Aspect
@Component
public class RateLimitAspect {

    private final HttpServletRequest request;
    private final RateLimiterRedisService rateLimiterRedisService;

    public RateLimitAspect(HttpServletRequest request, RateLimiterRedisService rateLimiterRedisService) {
        this.request = request;
        this.rateLimiterRedisService = rateLimiterRedisService;
    }

    @Before("@annotation(RateLimitCheck)")
    public void rateLimitCheck() {
        String clientIp = ClientIpHolder.getClientIp();
        String key = "rate_limit:" + clientIp;
        long current = rateLimiterRedisService.incrementAndGet(key, 60);
        if (current > 50) {
            throw new TooManyRequestException();
        }
    }

}
