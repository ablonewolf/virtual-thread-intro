package org.ablonewolf.completableFuture;

import org.ablonewolf.externalService.Client;
import org.ablonewolf.util.ThreadUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;

public class FetchingProducts {

    private static final Logger logger = LoggerFactory.getLogger(FetchingProducts.class);

    void main() {

        var virtualThreadFactory = Thread.ofVirtual().name("Virtual Thread - ", 1).factory();
        var executor = Executors.newThreadPerTaskExecutor(virtualThreadFactory);
        try (executor) {
            for (int i = 1; i <= 50; i++) {
                final int productId = i;
                var product = CompletableFuture.supplyAsync(() -> Client.getProduct(productId), executor);
                product.thenAccept(value -> logger.info("Product {} is {}", productId, value));
            }
        }

        ThreadUtils.sleep(Duration.ofSeconds(5));
    }
}
