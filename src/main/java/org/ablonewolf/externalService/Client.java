package org.ablonewolf.externalService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URI;
import java.util.Objects;

import static org.ablonewolf.externalService.APIConstants.PRODUCT_REQUEST_FORMAT;
import static org.ablonewolf.externalService.APIConstants.RATING_REQUEST_FORMAT;

public class Client {

    private static final Logger logger = LoggerFactory.getLogger(Client.class);

    public static String getProduct(int productId) {
        return callExternalService(PRODUCT_REQUEST_FORMAT.formatted(productId));
    }

    public static Integer getRatingForProduct(int productId) {
        String rating = callExternalService(RATING_REQUEST_FORMAT.formatted(productId));
        if (Objects.nonNull(rating)) {
            return Integer.parseInt(rating);
        }
        return null;
    }

    private static String callExternalService(String url) {
        logger.info("Calling the service on the url: {}", url);
        try (var stream = URI.create(url).toURL().openStream()) {
            return new String(stream.readAllBytes());
        } catch (IOException e) {
            logger.error("An error occurred while calling the external service on this url {}, error details: {}", url,
                e.getMessage());
            return null;
        }
    }
}
