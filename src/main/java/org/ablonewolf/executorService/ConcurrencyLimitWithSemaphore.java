package org.ablonewolf.executorService;

import org.ablonewolf.externalService.Client;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Executors;

public class ConcurrencyLimitWithSemaphore {

    private static final Logger logger = LoggerFactory.getLogger(ConcurrencyLimitWithSemaphore.class);

    void main() {
        var virtualThreadFactory = Thread.ofVirtual().name("Virtual Thread - ", 1).factory();
        try (var executorService = Executors.newThreadPerTaskExecutor(virtualThreadFactory)) {
            CustomConcurrencyLimiter customConcurrencyLimiter = new CustomConcurrencyLimiter(executorService, 3);
            fetchProductThroughAPICall(customConcurrencyLimiter, 30);
        }
    }

    private static void fetchProductThroughAPICall(CustomConcurrencyLimiter concurrencyLimiter, int taskCount) {
        try (concurrencyLimiter) {
            for (int i = 1; i <= taskCount; i++) {
                final int productNumber = i;
                concurrencyLimiter.submit(() -> printProductInfo(productNumber));
            }
            logger.info("submitted");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static String printProductInfo(int productId) {
        var product = Client.getProduct(productId);
        logger.info("Product info for the id {}: {}", productId, product);
        return product;
    }
}
