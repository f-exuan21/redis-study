package com.app.movie.application;


import com.app.movie.presentation.BookingRestController;
import com.app.movie.presentation.DuplicateBookingException;
import com.app.movie.presentation.dto.BookingRequestDto;
import com.app.movie.repository.BookingRepository;
import com.app.movie.repository.SeatRepository;
import com.app.movie.repository.ShowtimeRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

@SpringBootTest
@Transactional
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class MovieBookingTest {

    @Autowired
    private BookingService bookingService;

    @Test
    public void 에약하기() throws InterruptedException {
        int threadCount = 10;
        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);

        CountDownLatch latch = new CountDownLatch(threadCount);
        BookingRequestDto bookingRequestDto = new BookingRequestDto(1L, 1L);

        // 예외 카운트용
        AtomicInteger successCount = new AtomicInteger(0);
        AtomicInteger failureCount = new AtomicInteger(0);

        IntStream.range(0, threadCount).forEach(i -> {
            executorService.submit(() -> {
                try {
                    bookingService.bookShowtime(bookingRequestDto);
                    successCount.incrementAndGet();
                    System.out.println("예약 성공");
                } catch (DuplicateBookingException e) {
                    System.out.println("중복 예약 실패: " + e.getMessage());
                    failureCount.incrementAndGet();
                } catch (Exception e) {
                    System.out.println("기타 에러: " + e.getMessage());
                } finally {
                    latch.countDown();
                }
            });
        });

        latch.await();
        executorService.shutdown();

        System.out.println("성공 수: " + successCount.get());
        System.out.println("실패 수: " + failureCount.get());

        // 검증: 성공은 1번만 허용, 나머지는 실패해야 함
        assert successCount.get() == 1 : "예약 성공은 딱 1번이어야 함";
        assert failureCount.get() == threadCount - 1 : "나머지는 중복 예약 실패여야 함";
    }

}
