import com.app.movie.MovieApplication;
import com.app.movie.entity.BookingEntity;
import com.app.movie.entity.SeatEntity;
import com.app.movie.entity.ShowtimeEntity;
import com.app.movie.repository.BookingJpaRepository;
import com.app.movie.repository.BookingRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest(classes = MovieApplication.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class RepositoryTest {

    @Autowired
    BookingJpaRepository bookingRepository;

    @Transactional
    public Long createTestBooking() {
        BookingEntity booking = new BookingEntity();
        booking.setSeat(new SeatEntity(10L));
        booking.setShowtime(new ShowtimeEntity(1L));
        booking.setUpdatedBy("USER");
        return bookingRepository.saveAndFlush(booking).getId();
    }


    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public BookingEntity fetchBookingInNewTransaction(Long id) {
        return bookingRepository.findById(id).orElseThrow();
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void updateBookingInNewTransaction(BookingEntity bookingEntity, String name) {
        bookingEntity.setUpdatedBy(name);
        bookingRepository.saveAndFlush(bookingEntity);
    }

    @Test
    void 낙관락테스트() {

        final Long testBookingId = createTestBooking();

        BookingEntity bookingEntity1 = fetchBookingInNewTransaction(testBookingId);
        BookingEntity bookingEntity2 = fetchBookingInNewTransaction(testBookingId);


        updateBookingInNewTransaction(bookingEntity1, "테스트1");

        Assertions.assertThrows(ObjectOptimisticLockingFailureException.class, () -> {
            updateBookingInNewTransaction(bookingEntity2, "테스트2");
        });
    }

}
