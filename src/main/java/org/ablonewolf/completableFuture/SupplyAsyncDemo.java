package org.ablonewolf.completableFuture;

import org.ablonewolf.util.ThreadUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;

public class SupplyAsyncDemo {

    private static final Logger logger = LoggerFactory.getLogger(SupplyAsyncDemo.class);

    void main() {
        logger.info("main method starts");

        supplyTask()
            .thenAccept(value -> logger.info("message: {}", value));

        logger.info("main method ends");

        ThreadUtils.sleep(Duration.ofSeconds(5));
    }

    private static CompletableFuture<String> supplyTask() {
        logger.info("supplyTask method starts");

        var virtualThreadFactory = Thread.ofVirtual().name("Virtual Thread").factory();
        var completableFuture = CompletableFuture.supplyAsync(() -> {
            ThreadUtils.sleep(Duration.ofSeconds(1));
            return "Task executed";
        }, Executors.newThreadPerTaskExecutor(virtualThreadFactory));

        logger.info("supplyTask method ends");
        return completableFuture;
    }
}
