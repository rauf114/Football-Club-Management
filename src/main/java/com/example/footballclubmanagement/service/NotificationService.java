package com.example.footballclubmanagement.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class NotificationService {

    @Async
    public void sendFileUploadNotification(String fileName, String recipientEmail) {
        log.info("Starting async email notification task on thread: {}", Thread.currentThread().getName());
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.error("Async email notification was interrupted", e);
        }
        log.info("Email successfully sent to {} for file: {}", recipientEmail, fileName);
    }
}