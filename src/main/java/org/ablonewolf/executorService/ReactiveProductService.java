package org.ablonewolf.executorService;

import org.ablonewolf.externalService.ProductHttpClient;
import org.ablonewolf.models.ProductDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Mono;

public class ReactiveProductService {

    private static final Logger logger = LoggerFactory.getLogger(ReactiveProductService.class);
    private final ProductHttpClient client = new ProductHttpClient();

    public Mono<ProductDTO> getProductInfo(int productId) {
        Mono<String> productMono = client.getProduct(productId)
            .doOnError(ex -> logger.error("Error fetching product name for ID {}: {}", productId, ex.getMessage()))
            .onErrorResume(ex -> {
                logger.warn("Returning default product name for ID {} due to erro {}r", productId, ex.getMessage());
                return Mono.just("Unknown Product"); // Fallback value instead of null
            });

        Mono<Integer> ratingMono = client.getRatingForProduct(productId)
            .doOnError(ex -> logger.error("Error fetching rating for product ID {}: {}", productId, ex.getMessage()))
            .onErrorResume(ex -> {
                logger.warn("Returning default rating for ID {} due to error {}", productId, ex.getMessage());
                return Mono.just(0); // Fallback value instead of null
            });

        return Mono.zip(productMono, ratingMono)
            .map(tuple -> new ProductDTO(productId, tuple.getT1(), tuple.getT2()))
            .doOnNext(productDTO -> logger.info("Constructed ProductDTO: {}", productDTO))
            .doOnError(ex -> logger.error("Error constructing ProductDTO for ID {}: {}", productId, ex.getMessage()))
            .onErrorResume(ex -> {
                logger.warn("Returning default ProductDTO for ID {} due to error {}", productId, ex.getMessage());
                return Mono.just(new ProductDTO(productId, "Unknown Product", 0)); // Fallback ProductDTO
            });
    }


}

