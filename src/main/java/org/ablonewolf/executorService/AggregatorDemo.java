package org.ablonewolf.executorService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Executors;

public class AggregatorDemo {

    private static final Logger logger = LoggerFactory.getLogger(AggregatorDemo.class);

    @SuppressWarnings("unused")
    void main() {
        var platformThreadFactory = Thread.ofPlatform().name("Platform Thread - ", 1).factory();
        var virtualThreadFactory = Thread.ofVirtual().name("Virtual Thread - ", 1).factory();
        var executorService = Executors.newThreadPerTaskExecutor(platformThreadFactory);
        var aggregatorService = new AggregatorService(executorService);

        var startTime = System.currentTimeMillis();

        for (int i = 1; i <= 500000; i++) {
            var productInfo = aggregatorService.getProductInfo(i);
            logger.info("product info: {}", productInfo);
            }

        var endTime = System.currentTimeMillis();
        logger.info("Elapsed time: {} ms", endTime - startTime);


    }
}
