package org.ablonewolf.externalService;

public final class APIConstants {

    private APIConstants() {

    }
    // All API formats

    public static final String PRODUCT_REQUEST_FORMAT = "http://localhost:7070/productApp/product/%s";
    public static final String RATING_REQUEST_FORMAT = "http://localhost:7070/productApp/rating/%s";
    public static final String PRODUCT_STREAMING_REQUEST_FORMAT = "http://localhost:7070/productApp/stream/products/%s";
    public static final String RATING_STREAMING_REQUEST_FORMAT = "http://localhost:7070/productApp/stream/ratings/%s";
}
