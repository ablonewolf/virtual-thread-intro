package org.ablonewolf.completableFuture;

import org.ablonewolf.util.ThreadUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.concurrent.Executors;

public class AggregatorDemo {

    private static final Logger logger = LoggerFactory.getLogger(AggregatorDemo.class);

    void main() {
        var virtualThreadFactory = Thread.ofVirtual().name("Virtual Thread - ", 1).factory();
        var executorService = Executors.newThreadPerTaskExecutor(virtualThreadFactory);

        try (executorService) {
            AggregatorService aggregatorService = new AggregatorService(executorService);

            for (int i = 1; i <= 50; i++) {
                aggregatorService.getProductInfo(i)
                    .thenAccept(product -> logger.info("Product: {}", product));
            }
        }

        ThreadUtils.sleep(Duration.ofSeconds(5));
    }
}
