package org.ablonewolf.externalService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class AccessResponseUsingFutureDemo {

    private static final Logger logger = LoggerFactory.getLogger(AccessResponseUsingFutureDemo.class);

    void main() {
        try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {

            try {
                /*
                 though the future.get() is blocking, internally JVM will place the virtual thread
                 to its heap memory when the blocking network call takes place,
                 and the OS will use its non-blocking API to fetch the desired data
                 after that, the concerned virtual thread will be placed inside the concerned platform thread
                 that's how the operation is non-blocking
                */
                List<Future<String>> products = new ArrayList<>();

                /*
                 * we are using separate for loops for the following reason
                 * we are logging the product in another for loop to show that the API calling is not blocking
                 * any of the platform threads; neither the get method of the future objects,
                 * so all the following sequential API calls happen concurrently
                 */
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
}
