package com.app.movie.presentation;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        StringBuilder errorMessage = new StringBuilder("[유효성 검증 실패] ");
        ex.getBindingResult().getFieldErrors().forEach(error -> errorMessage.append(
                String.format("필드 %s : %s. ", error.getField(), error.getDefaultMessage())
        ));
        return new ResponseEntity<>(errorMessage.toString(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ShowtimeNotFoundException.class)
    public ResponseEntity<String> handleShowtimeNotFoundException(ShowtimeNotFoundException e) {
        return new ResponseEntity<>("해당 Showtime 데이터는 존재하지 않음. Showtime ID : " + e.getShowtimeId(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(SeatNotFoundException.class)
    public ResponseEntity<String> handleSeatNotFoundException(SeatNotFoundException e) {
        return new ResponseEntity<>("해당 Seat 데이터는 존재하지 않음. Seat ID : " + e.getSeatId(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DuplicateBookingException.class)
    public ResponseEntity<String> handleDuplicateBookingException(DuplicateBookingException e) {
        return new ResponseEntity<>("이미 예약된 영화 및 좌석임.", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidSeatSelectionException.class)
    public ResponseEntity<String> handleInvalidSeatSelectionException(InvalidSeatSelectionException e) {
        return new ResponseEntity<>("좌석은 연속적이고 동일 라인 내에 있어야 합니다.", HttpStatus.BAD_REQUEST);
    }
}
