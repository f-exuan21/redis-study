package com.app.movie.entity;


import com.app.movie.mapper.SeatMapper;
import com.app.movie.mapper.ShowtimeMapper;
import com.app.movie.model.Booking;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Booking")
@Getter
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

    @CreatedDate
    private LocalDateTime createdAt;

    @Setter
    private String updatedBy;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    @Version
    private Long version;

    public BookingEntity(Long id) {
        this.id = id;
    }

    public BookingEntity(ShowtimeEntity showtime, SeatEntity seat, String createdBy) {
        this.showtime = showtime;
        this.seat = seat;
        this.createdBy = createdBy;
    }


    public static BookingEntity of(Booking booking) {
        ShowtimeEntity showtimeEntity = new ShowtimeEntity(booking.getShowtime().getId());
        SeatEntity seatEntity = new SeatEntity(booking.getSeat().getId());

        BookingEntity bookingEntity = new BookingEntity(
                showtimeEntity,
                seatEntity,
                "tester"
        );

        return bookingEntity;
    }

    public Booking toBooking() {
        Booking booking = new Booking(
                id,
                ShowtimeMapper.toDomain(showtime),
                SeatMapper.toDomain(seat)
        );
        return booking;
    }


}
