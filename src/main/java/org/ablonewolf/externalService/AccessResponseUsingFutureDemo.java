package org.ablonewolf.externalService;

import org.ablonewolf.util.CommonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadFactory;

public class AccessResponseUsingFutureDemo {

    private static final Logger logger = LoggerFactory.getLogger(AccessResponseUsingFutureDemo.class);

    void main() {

        ThreadFactory threadFactory = Thread.ofPlatform().name("Platform thread - ", 1).factory();
        try (var executor = Executors.newThreadPerTaskExecutor(threadFactory)) {
            // this will be blocking since we are using platform threads.
            var totalTime = CommonUtils.timer(() -> fetchProductFromAPI(executor));
            logger.info("total time using cached threads: {}", totalTime);
            executor.shutdown();
        }

        try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
            // this will be non-blocking since we are using the virtual threads per task
            var totalTime = CommonUtils.timer(() -> fetchProductFromAPI(executor));
            logger.info("total time using virtual threads: {}", totalTime);
            executor.shutdown();
        }
    }

    private void fetchProductFromAPI(ExecutorService executor) {
        try {
            List<Future<String>> products = new ArrayList<>();
            for (int i = 1; i <= 50; i++) {
                final var productId = i;
                products.add(executor.submit(() -> Client.getProduct(productId)));
            }
            for (var productInfo : products) {
                logger.info("Product name: {}", productInfo.get());
            }

        } catch (InterruptedException | ExecutionException e) {
            logger.error("An error occurred while fetching the product info from the future object, details: {}",
                e.getMessage());
        }
    }
}
