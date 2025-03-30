package com.app.movie.entity;


import com.app.movie.model.Booking;
import com.app.movie.model.Genre;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Booking")
@Getter
@Setter
public class BookingEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "showtime_id")
    private ShowtimeEntity showtime;

    @ManyToOne
    @JoinColumn(name = "seat_id")
    private SeatEntity seat;

    private String createdBy;
    private LocalDateTime createdAt;
    private String updatedBy;
    private LocalDateTime updatedAt;

    @Version
    private Long version;

    public SeatEntity getSeat() {
        return seat;
    }

    public void setSeat(SeatEntity seat) {
        this.seat = seat;
    }

    public ShowtimeEntity getShowtime() {
        return showtime;
    }

    public void setShowtime(ShowtimeEntity showtime) {
        this.showtime = showtime;
    }

    public static BookingEntity of(Booking booking) {
        ShowtimeEntity showtimeEntity = new ShowtimeEntity(booking.getShowtime().getId());
        SeatEntity seatEntity = new SeatEntity(booking.getSeat().getId());

        BookingEntity bookingEntity = new BookingEntity();
        bookingEntity.setId(bookingEntity.getId());
        bookingEntity.setSeat(seatEntity);
        bookingEntity.setShowtime(showtimeEntity);
        bookingEntity.setCreatedBy("tester");
        return bookingEntity;
    }


}
