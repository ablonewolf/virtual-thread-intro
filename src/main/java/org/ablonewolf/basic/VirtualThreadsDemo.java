package org.ablonewolf.basic;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.time.Instant;

public class VirtualThreadsDemo {
    private static final Logger log = LoggerFactory.getLogger(VirtualThreadsDemo.class);

    public static void main() {
        Instant start = Instant.now();

        // Create 10,000 virtual threads
        for (int i = 0; i < 10_000; i++) {
            Thread.ofVirtual().start(() -> {
                try {
                    // Simulate a blocking operation
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    log.error("Thread was interrupted due to this {}", e.getMessage());
                }
            });
        }

        // Record the end time
        Instant end = Instant.now();

        // Print the time taken to spawn the threads
        log.info("Total time: {} ms", Duration.between(start, end).toMillis());

        // Keep the program running to observe the threads (optional)
        try {
            Thread.sleep(2000); // Sleep for 2 seconds
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}

