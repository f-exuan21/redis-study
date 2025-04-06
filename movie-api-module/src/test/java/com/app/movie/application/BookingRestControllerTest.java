package com.app.movie.application;

import com.app.movie.presentation.dto.BookingRequestDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class BookingRestControllerTest {

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Test
    void 영화_예매_성공() {
        //given
        String url = "/book";
        BookingRequestDto bookingRequestDto = new BookingRequestDto(2L, List.of(3L, 4L));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<BookingRequestDto> requestEntity = new HttpEntity<>(bookingRequestDto, headers);

        //when
        ResponseEntity<Void> response = testRestTemplate
                .postForEntity(url, requestEntity, Void.class);

        //then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void 영화_예매_실패() {
        //given
        String url = "/book";
        BookingRequestDto bookingRequestDto = new BookingRequestDto(3L, List.of(3L, 7L));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<BookingRequestDto> requestEntity = new HttpEntity<>(bookingRequestDto, headers);

        //when
        ResponseEntity<String> response = testRestTemplate
                .postForEntity(url, requestEntity, String.class);

        //then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).contains("좌석은 연속적이고 동일 라인 내에 있어야 합니다.");
    }

}
