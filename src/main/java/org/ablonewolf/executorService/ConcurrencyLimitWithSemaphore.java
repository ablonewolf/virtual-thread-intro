package org.ablonewolf.executorService;

import org.ablonewolf.util.CommonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Executors;

public class ConcurrencyLimitWithSemaphore {

    private static final Logger logger = LoggerFactory.getLogger(ConcurrencyLimitWithSemaphore.class);

    void main() {
        var virtualThreadFactory = Thread.ofVirtual()
            .name("Virtual Thread - ", 1)
            .factory();
        try (var executorService = Executors.newThreadPerTaskExecutor(virtualThreadFactory)) {
            CustomConcurrencyLimiter<String> customConcurrencyLimiter =
                new CustomConcurrencyLimiter<>(executorService, 3);
            fetchProductThroughAPICall(customConcurrencyLimiter, 30);
        }
    }

    private static void fetchProductThroughAPICall(CustomConcurrencyLimiter<String> concurrencyLimiter, int taskCount) {
        try (concurrencyLimiter) {
            for (int i = 1; i <= taskCount; i++) {
                final int productNumber = i;
                concurrencyLimiter.submit(() -> CommonUtils.printProductInfo(productNumber, logger));
            }
            logger.info("submitted");
        } catch (Exception e) {
            logger.error("An error occurred while submitting the task to the concurrency limiter, details: {}",
                e.getMessage());
        }
    }
}
