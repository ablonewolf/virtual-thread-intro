package org.ablonewolf.completableFuture;

import org.ablonewolf.util.ThreadUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.concurrent.CompletableFuture;

public class RunAsyncAndExceptionallyDemo {

    private static final Logger logger = LoggerFactory.getLogger(RunAsyncAndExceptionallyDemo.class);

    void main() {
        logger.info("main method starts");

        demoRunAsyncTask()
            .thenRun(() -> logger.info("Task completed"));

        demoExceptionally()
            .exceptionally(ex -> {
                logger.error("An error occurred, details: {}", ex.getMessage());
                return null;
            });

        logger.info("main method ends");

        ThreadUtils.sleep(Duration.ofSeconds(10));
    }

    private static CompletableFuture<Void> demoRunAsyncTask() {
        logger.info("demoRunAsyncTask method starts");

        var completeFuture = CompletableFuture.runAsync(() -> {
            ThreadUtils.sleep(Duration.ofSeconds(3));
            logger.info("Task got executed");
        });

        logger.info("demoRunAsyncTask method ends");
        return completeFuture;
    }

    private static CompletableFuture<Void> demoExceptionally() {
        logger.info("demoExceptionally method starts");

        var completeFuture = CompletableFuture.runAsync(() -> {
            ThreadUtils.sleep(Duration.ofSeconds(3));
            throw new RuntimeException("An error occurred");
        });

        logger.info("demoExceptionally method ends");
        return completeFuture;
    }
}
