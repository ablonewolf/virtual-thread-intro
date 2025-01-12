package org.ablonewolf.executorService;

import org.ablonewolf.util.ThreadUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;

import java.time.Duration;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.stream.IntStream;

public class ReactiveAggregatorDemo {

    private static final Logger logger = LoggerFactory.getLogger(ReactiveAggregatorDemo.class);
    private static final int BATCH_SIZE = 1000;

    void main() {
        ThreadFactory virtualThreadFactory = Thread.ofPlatform().name("Platform Thread - ", 1).factory();
        ExecutorService executorService = Executors.newThreadPerTaskExecutor(virtualThreadFactory);
        Scheduler scheduler = Schedulers.fromExecutor(executorService);

        var startTime = System.currentTimeMillis();

        try (executorService) {
            var productService = new ReactiveProductService();

            for (int i = 0; i < 500000; i += BATCH_SIZE) {
                int end = Math.min(i + BATCH_SIZE, 500000);

                // Submit tasks in batches
                submitTasksInBatch(i, end, productService, scheduler);
            }

            // Sleep for a while to let tasks complete
            ThreadUtils.sleep(Duration.ofSeconds(5));

        } catch (Exception e) {
            logger.error("An error occurred: {}", e.getMessage());
            Thread.currentThread().interrupt();
        }

        var endTime = System.currentTimeMillis();
        logger.info("Elapsed time: {} ms", endTime - startTime);
    }

    private static void submitTasksInBatch(int start, int end, ReactiveProductService productService,
                                           Scheduler scheduler) {
        IntStream.range(start, end)
            .forEach(index -> fetchAndPrintProductInfo(index, productService, scheduler)
                .subscribe());
    }

    private static Mono<Void> fetchAndPrintProductInfo(int productId, ReactiveProductService productService,
                                                       Scheduler scheduler) {
        return productService.getProductInfo(productId)
            .subscribeOn(scheduler)
            .doOnNext(productDTO -> logger.info("Product: {}", productDTO))
            .doOnError(ex -> logger.error("Error occurred while printing product info, details: {}", ex.getMessage()))
            .then();
    }
}
