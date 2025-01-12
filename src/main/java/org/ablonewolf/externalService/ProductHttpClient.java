package org.ablonewolf.externalService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Mono;

import static org.ablonewolf.externalService.APIConstants.PRODUCT_REQUEST_FORMAT;
import static org.ablonewolf.externalService.APIConstants.RATING_REQUEST_FORMAT;

public class ProductHttpClient extends AbstractHttpClient {

    private static final Logger logger = LoggerFactory.getLogger(ProductHttpClient.class);

    public Mono<String> getProduct(int productId) {
        return httpClient
            .baseUrl(PRODUCT_REQUEST_FORMAT.formatted(productId))
            .get()
            .responseSingle((response, byteBufMono) -> {
                if (response.status().code() == 200) {
                    return byteBufMono.asString();
                } else {
                    return Mono.error(new RuntimeException("Failed to fetch product information"));
                }
            })
            .doOnError(
                ex -> logger.error("An error occurred while fetching the product info, details: {}", ex.getMessage()))
            .onErrorReturn("Unknown Product");  // Changed from null to default value
    }

    public Mono<Integer> getRatingForProduct(int productId) {
        return httpClient
            .baseUrl(RATING_REQUEST_FORMAT.formatted(productId))
            .get()
            .responseSingle((response, byteBufMono) -> {
                if (response.status().code() == 200) {
                    return byteBufMono.asString()
                        .map(Integer::parseInt);
                } else {
                    return Mono.error(new RuntimeException("Failed to fetch rating information"));
                }
            })
            .doOnError(
                ex -> logger.error("An error occurred while fetching the rating info, details: {}", ex.getMessage()))
            .onErrorReturn(0);  // Changed from null to default value
    }
}

