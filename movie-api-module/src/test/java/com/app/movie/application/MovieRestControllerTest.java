package com.app.movie.application;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class MovieRestControllerTest {

    @Autowired
    TestRestTemplate testRestTemplate;


    @Test
    public void 영화_리스트_조회() throws Exception {

        // given

        // when
        var response = testRestTemplate.getForEntity(
                "/movie?title=&genres=드라마",
                String.class
        );

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).contains("드라마");

    }


}