package org.ablonewolf.completableFuture;

import org.ablonewolf.models.ProductDTO;
import org.ablonewolf.util.ThreadUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;

public class AggregatorDemo {

    private static final Logger logger = LoggerFactory.getLogger(AggregatorDemo.class);

    void main() {
        var virtualThreadFactory = Thread.ofVirtual().name("Virtual Thread - ", 1).factory();
        var executorService = Executors.newThreadPerTaskExecutor(virtualThreadFactory);

        try (executorService) {
            AggregatorService aggregatorService = new AggregatorService(executorService);
            List<CompletableFuture<ProductDTO>> futures = new ArrayList<>();

            for (int i = 1; i <= 50; i++) {
                futures.add(aggregatorService.getProductInfo(i));
            }

            CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]))
                .thenRun(() -> {
                    futures.forEach(future ->
                        future.thenAccept(product -> logger.info("Product: {}", product)));
                    logger.info("All products have been fetched.");
                })
                .exceptionally(ex -> {
                    logger.error("An error occurred while fetching products, details: {}", ex.getMessage());
                    return null;
                });
        }


        ThreadUtils.sleep(Duration.ofSeconds(5));
    }
}
