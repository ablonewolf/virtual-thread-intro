package org.ablonewolf.executorService;

import lombok.RequiredArgsConstructor;
import org.ablonewolf.externalService.Client;
import org.ablonewolf.models.ProductDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;

@RequiredArgsConstructor
public class AggregatorService {

    private static final Logger logger = LoggerFactory.getLogger(AggregatorService.class);
    private final ExecutorService executorService;

    public ProductDTO getProductInfo(int productId) {
        var productName = executorService.submit(() -> Client.getProduct(productId));
        var productRating = executorService.submit(() -> Client.getRatingForProduct(productId));

        try {
            if (Objects.nonNull(productName.get()) && Objects.nonNull(productRating.get())) {
                return new ProductDTO(productId, productName.get(), productRating.get());
            }
        } catch (InterruptedException | ExecutionException e) {
            logger.error("An error occurred while retrieving product information, details: {}", e.getMessage());
        }
        return null;
    }
}
