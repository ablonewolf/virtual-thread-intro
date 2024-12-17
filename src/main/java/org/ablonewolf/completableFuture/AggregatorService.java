package org.ablonewolf.completableFuture;

import lombok.RequiredArgsConstructor;
import org.ablonewolf.externalService.Client;
import org.ablonewolf.models.ProductDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;

@RequiredArgsConstructor
public class AggregatorService {

    private static final Logger logger = LoggerFactory.getLogger(AggregatorService.class);
    private final ExecutorService executorService;

    public CompletableFuture<ProductDTO> getProductInfo(int productId) {
        var productFuture = CompletableFuture.
            supplyAsync(() -> Client.getProduct(productId), executorService)
            .exceptionally(ex -> {
                logger.error("An error occurred while fetching the product name, details: {}", ex.getMessage());
                return null;
            });

        var ratingFuture = CompletableFuture
            .supplyAsync(() -> Client.getRatingForProduct(productId), executorService)
            .exceptionally(ex -> {
                logger.error("An error occurred while fetching the rating for the product {}, details: {}", productId,
                    ex.getMessage());
                return null;
            });

        return productFuture.thenCombine(ratingFuture, (product, rating) ->
                new ProductDTO(productId, product, rating))
            .exceptionally(ex -> {
                logger.error("An error occurred, details: {}", ex.getMessage());
                return null;
            });
    }
}
