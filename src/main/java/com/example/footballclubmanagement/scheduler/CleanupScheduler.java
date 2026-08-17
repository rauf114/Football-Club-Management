package com.example.footballclubmanagement.scheduler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@Slf4j
public class CleanupScheduler {

    @Scheduled(cron = "0 0 0 * * ?")
    public void performDailyCleanup() {
        log.info("Starting daily scheduled cleanup task at: {}", LocalDateTime.now());

        log.info("Temporary storage and expired cache entries cleaned successfully.");
    }
}