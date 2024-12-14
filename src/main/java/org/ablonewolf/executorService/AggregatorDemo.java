package org.ablonewolf.executorService;

import org.ablonewolf.models.ProductDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.IntStream;

public class AggregatorDemo {

    private static final Logger logger = LoggerFactory.getLogger(AggregatorDemo.class);

    void main() {
        try (var executorService = Executors.newVirtualThreadPerTaskExecutor()) {
            var aggregatorService = new AggregatorService(executorService);

            var productFutures = IntStream.rangeClosed(1, 50)
                .mapToObj(id -> executorService.submit(() -> aggregatorService.getProductInfo(id)))
                .toList();

            var productList = productFutures.stream()
                .map(AggregatorDemo::toProductDTO)
                .toList();

            for (var product : productList) {
                logger.info("Product info: {}", product);
            }

        }
    }

    private static ProductDTO toProductDTO(Future<ProductDTO> productFuture) {
        try {
            return productFuture.get();
        } catch (InterruptedException | ExecutionException e) {
            logger.error("Error occurred while retrieving product info from the future, details: {}", e.getMessage());
        }
        return null;
    }
}
