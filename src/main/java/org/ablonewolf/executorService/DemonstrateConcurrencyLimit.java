package org.ablonewolf.executorService;

import org.ablonewolf.externalService.Client;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DemonstrateConcurrencyLimit {

    private static final Logger logger = LoggerFactory.getLogger(DemonstrateConcurrencyLimit.class);

    void main() {
        var virtualThreadFactory = Thread.ofVirtual().name("Virtual Thread - ", 1).factory();
        fetchProductThroughAPICall(Executors.newFixedThreadPool(3, virtualThreadFactory), 30);
    }

    private static void fetchProductThroughAPICall(ExecutorService executorService, int taskCount) {
        try (executorService) {
            for (int i = 1; i <= taskCount; i++) {
                final int productNumber = i;
                executorService.submit(() -> printProductInfo(productNumber));
            }
            logger.info("submitted");
        }
    }

    private static void printProductInfo(int productId) {
        logger.info("Product info for the id {}: {}", productId, Client.getProduct(productId));
    }
}
