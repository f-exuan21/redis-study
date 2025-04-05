package com.app.movie.aop;

import com.app.movie.presentation.TooManyRequestException;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

@Aspect
@Component
public class RateLimitAspect {

    private final HttpServletRequest request;

    private final Cache<String, AtomicInteger> requestCountsPerIpAddress = CacheBuilder.newBuilder()
            .expireAfterWrite(1, TimeUnit.MINUTES)
            .build();

    private final Cache<String, Boolean> blockedIpAddress = CacheBuilder.newBuilder()
            .expireAfterWrite(1, TimeUnit.HOURS)
            .build();

    public RateLimitAspect(HttpServletRequest request) {
        this.request = request;
    }

    private String getClientIpAddress() {
        String ipAddress = request.getHeader("X-Forwarded-For");
        if(ipAddress == null || ipAddress.isEmpty()) {
            return "";
        }

        return ipAddress.split(",")[0];
    }

    @Before("@annotation(RateLimitCheck)")
    public void rateLimitCheck() {
        String clientIp = this.getClientIpAddress();

        if (blockedIpAddress.getIfPresent(clientIp) != null) {
            throw new TooManyRequestException();
        }

        try {
            AtomicInteger requestCount = requestCountsPerIpAddress.get(clientIp, AtomicInteger::new);
            if (requestCount.incrementAndGet() > 50) {
                blockedIpAddress.put(clientIp, true);
                throw new TooManyRequestException();
            }
        } catch (ExecutionException e) {
            throw new RuntimeException("요청 수 체크 중 오류가 발생했습니다.", e);
        }

    }

}
