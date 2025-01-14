package org.ablonewolf.externalService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

import java.time.Duration;

import static org.ablonewolf.externalService.APIConstants.PRODUCT_STREAMING_REQUEST_FORMAT;
import static org.ablonewolf.externalService.APIConstants.RATING_STREAMING_REQUEST_FORMAT;

public class ProductHttpClient extends AbstractHttpClient {

    private static final Logger logger = LoggerFactory.getLogger(ProductHttpClient.class);

    private static void showRetryAttemptMessage(Retry.RetrySignal signal) {
        logger.warn("Retrying due to this error: {}",
            signal.failure().getMessage());
    }

    public Mono<String> getProduct(int productId) {
        return httpClient
            .get()
            .uri(PRODUCT_STREAMING_REQUEST_FORMAT.formatted(productId))
            .responseContent()
            .asString()
            .doOnError(
                ex -> logger.error("An error occurred while fetching the product name, details: {}",
                    ex.getMessage()))
            .retryWhen(Retry.fixedDelay(3, Duration.ofMillis(500L))
                .doBeforeRetry(ProductHttpClient::showRetryAttemptMessage))
            .next();  // Changed from null to default value
    }

    public Mono<String> getRatingForProduct(int productId) {
        return httpClient
            .get()
            .uri(RATING_STREAMING_REQUEST_FORMAT.formatted(productId))
            .responseContent()
            .asString()
            .doOnError(
                ex -> logger.error("An error occurred while fetching the rating, details: {}",
                    ex.getMessage()))
            .retryWhen(Retry.fixedDelay(3, Duration.ofMillis(500L))
                .doBeforeRetry(ProductHttpClient::showRetryAttemptMessage))
            .next();   // Changed from null to default value
    }
}

