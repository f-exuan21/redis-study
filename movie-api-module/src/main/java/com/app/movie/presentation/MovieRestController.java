package com.app.movie.presentation;

import com.app.movie.application.MovieService;
import com.app.movie.presentation.dto.MovieRequestDto;
import com.app.movie.presentation.dto.MovieResponseDto;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

@RestController
@RequestMapping("/movie")
public class MovieRestController {

    private final MovieService movieService;

    private final Cache<String, AtomicInteger> requestCountsPerIpAddress = CacheBuilder.newBuilder()
            .expireAfterWrite(1, TimeUnit.MINUTES)
            .build();

    private final Cache<String, Boolean> blockedIpAddress = CacheBuilder.newBuilder()
            .expireAfterWrite(1, TimeUnit.HOURS)
            .build();

    @Autowired
    public MovieRestController(MovieService movieService) {
        this.movieService = movieService;
    }

    @GetMapping()
    public List<MovieResponseDto> getMovies(@RequestHeader(value = "X-Forwarded-For", required = false) String ipAddress,
                                            @Valid @ModelAttribute MovieRequestDto movieRequestDto) {

        String clientIp = this.getClientIpAddress(ipAddress);

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

        return movieService.getAllMovies(movieRequestDto);
    }

    private String getClientIpAddress(String ipAddress) {

        if(ipAddress == null || ipAddress.isEmpty()) {
            return "";
        }

        return ipAddress.split(",")[0];
    }
}
