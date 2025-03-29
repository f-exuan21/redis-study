package com.app.movie.application;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class FcmMessageSender implements MessageSender{

    public void send() {
        try {
            Thread.sleep(500);
            log.info("FCM MESSAGE PUSH SUCCESS");
        } catch (InterruptedException e) {
            log.info("FCM MESSAGE PUSH FAILURE");
            Thread.currentThread().interrupt();
        }

    }

}
