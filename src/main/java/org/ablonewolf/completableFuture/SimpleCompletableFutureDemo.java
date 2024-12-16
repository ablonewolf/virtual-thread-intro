package org.ablonewolf.completableFuture;

import org.ablonewolf.util.ThreadUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.concurrent.CompletableFuture;

public class SimpleCompletableFutureDemo {

    private static final Logger logger = LoggerFactory.getLogger(SimpleCompletableFutureDemo.class);

    void main() {
        logger.info("Main method starts");

        var completableFuture = demoTask();
        completableFuture.thenAccept(value -> logger.info("message: {}", value));

        logger.info("Main method ends");

        // making the main thread sleep so that we get the response from the completable future object
        // as the future will complete its execution inside a virtual thread which is itself a daemon thread
        ThreadUtils.sleep(Duration.ofSeconds(4));
    }

    private static CompletableFuture<String> demoTask() {
        logger.info("Method starts");

        var completableFuture = new CompletableFuture<String>();
        Thread.ofVirtual().name("Virtual Thread").start(() -> {
            ThreadUtils.sleep(Duration.ofSeconds(2));
            completableFuture.complete("task completed");
        });

        logger.info("Method ends");
        return completableFuture;
    }
}
