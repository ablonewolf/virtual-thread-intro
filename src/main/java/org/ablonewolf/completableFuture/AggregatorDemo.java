package org.ablonewolf.completableFuture;

import org.ablonewolf.util.ThreadUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.concurrent.Executors;

public class AggregatorDemo {

    private static final Logger logger = LoggerFactory.getLogger(AggregatorDemo.class);

    @SuppressWarnings("unused")
    void main() {
        var platformThreadFactory = Thread.ofPlatform().name("Platform Thread - ", 1).factory();
        var virtualThreadFactory = Thread.ofVirtual().name("Virtual Thread - ", 1).factory();
        var executorService = Executors.newThreadPerTaskExecutor(virtualThreadFactory);

        var startTime = System.currentTimeMillis();
        try (executorService) {
            AggregatorService aggregatorService = new AggregatorService(executorService);

            for (int i = 1; i <= 500000; i++) {
                fetchAndPrintProductInfo(i, aggregatorService);
            }

        }
        // we will prefer this approach when we need to fetch each product in an order
//        List<CompletableFuture<ProductDTO>> productFutures = new ArrayList<>();
//        CompletableFuture.allOf(productFutures.toArray(new CompletableFuture[0]))
//            .thenRun(() -> {
//                productFutures.forEach(future ->
//                    future.thenAccept(product -> logger.info("Product: {}", product)));
//                logger.info("All products have been fetched.");
//            })
//            .exceptionally(ex -> {
//                logger.error("An error occurred while fetching products, details: {}", ex.getMessage());
//                return null;
//            });

        var endTime = System.currentTimeMillis();
        logger.info("Elapsed time: {} ms", endTime - startTime);

        ThreadUtils.sleep(Duration.ofSeconds(20));
    }

    private void fetchAndPrintProductInfo(Integer productId, AggregatorService aggregatorService) {
        var productFuture = aggregatorService.getProductInfo(productId);
        productFuture.thenAccept(productDTO -> logger.info("Product: {}", productDTO))
            .exceptionally(ex -> {
                logger.error("error occurred while printing product info, details: {}", ex.getMessage());
                return null;
            });
    }
}
