package com.app.movie.application;

import com.app.movie.presentation.BookingFrequencyLimitException;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.concurrent.TimeUnit;

@Service
public class RateLimiterRedisService {

    private final StringRedisTemplate redisTemplate;
    private final DefaultRedisScript<Long> rateLimitScript;

    public RateLimiterRedisService(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
        this.rateLimitScript = new DefaultRedisScript<>();
        this.rateLimitScript.setScriptText(
                "local current = redis.call('incr', KEYS[1]) " +
                "if tonumber(current) == 1 then " +
                    "redis.call('expire', KEYS[1], tonumber(ARGV[1])) " +
                "end " +
                "return current"
        );
        this.rateLimitScript.setResultType(Long.class);
    }

    public long incrementAndGet(String key, long expiredSeconds) {
        Long current = redisTemplate.execute(rateLimitScript, Collections.singletonList(key), String.valueOf(expiredSeconds));
        if (current == null) {
            throw new IllegalStateException("Lua Script 결과가 Null 입니다.");
        }
        return current;
    }

    public void setIfAbsent(String key, long expiredSeconds) {
        // success 가 true 이면, 저장에 성공했다는 뜻 -> 그 전에 해당 key로 값이 없었음
        // success 가 false 면, 이미 저장되어 있는 키가 있다는 뜻 -> 그 전에 해당 key로 값이 있었음
        Boolean success = redisTemplate.opsForValue().setIfAbsent(key, "1", expiredSeconds, TimeUnit.SECONDS);
        if (success == null) {
            throw new IllegalStateException("Lua Script 결과가 Null 입니다.");
        }
        if (!success) {
            throw new BookingFrequencyLimitException();
        }
    }
}
