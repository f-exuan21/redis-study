package com.app.movie.application;

import com.app.movie.MovieApplication;
import com.app.movie.model.Seat;
import com.app.movie.model.Showtime;
import com.app.movie.presentation.dto.BookingRequestDto;
import com.app.movie.repository.BookingRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@SpringBootTest(classes = MovieApplication.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class BookingConcurrencyTest {

    @Autowired
    private BookingService bookingService;

    @Autowired
    private BookingRepository bookingRepository;

    private Long testShowtimeId = 1L;
    private List<Long> testSeatId = Arrays.asList(1L, 2L, 3L);

    @Test
    void 동시에_같은_좌석_예약시_하나만_성공해야_한다() throws InterruptedException {
        int threadCount = 10;
        ExecutorService executor = Executors.newFixedThreadPool(threadCount);
        CountDownLatch latch = new CountDownLatch(threadCount);

        for (int i = 0; i < threadCount; i++) {
            executor.execute(() -> {
                try {
                    BookingRequestDto bookingRequestDto = new BookingRequestDto(testShowtimeId, testSeatId);
                    bookingService.bookShowtime(bookingRequestDto);
                } catch (Exception e) {
                    System.out.println("예외 발생: " + e.getMessage());
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await(); // 모든 스레드 작업 대기
        executor.shutdown();

        for (Long seatId : testSeatId) {
            long bookingCount = bookingRepository.countByShowtimeIdAndSeatId(testShowtimeId, seatId);
            Assertions.assertEquals(1, bookingCount); // 하나만 예약됐는지 확인
        }

    }


}
