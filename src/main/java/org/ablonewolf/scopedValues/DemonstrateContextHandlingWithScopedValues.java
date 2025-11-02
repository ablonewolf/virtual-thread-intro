package org.ablonewolf.scopedValues;

import org.ablonewolf.util.ThreadUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.UUID;

/**
 * Demonstrates context handling using Scoped Values in a web application scenario.
 */
public class DemonstrateContextHandlingWithScopedValues {

    private static final Logger log = LoggerFactory.getLogger(DemonstrateContextHandlingWithScopedValues.class);
    private static final ScopedValue<String> AUTH_TOKENS = ScopedValue.newInstance();

    static void main() {
        Thread.ofVirtual().name("user-1").start(() ->
                authFilter(DemonstrateContextHandlingWithScopedValues::orderController));
        Thread.ofVirtual().name("user-2").start(() ->
                authFilter(DemonstrateContextHandlingWithScopedValues::orderController));

        ThreadUtils.sleep(Duration.ofSeconds(2));
    }

    private static void authFilter(Runnable task) {
        ScopedValue.where(AUTH_TOKENS, authenticate())
                .run(task);
    }

    // Security
    private static String authenticate() {
        var token = UUID.randomUUID().toString();
        log.info("Generated session token: {}", token);
        return token;
    }

    // @Principal
    // POST /orders
    private static void orderController() {
        log.info("orderController: {}", AUTH_TOKENS.get());
        orderService();
    }

    private static void orderService() {
        log.info("orderService: {}", AUTH_TOKENS.get());

        // rebinding tokens for product service and inventory service calls
        ScopedValue.where(AUTH_TOKENS, "product-service-token")
                .run(DemonstrateContextHandlingWithScopedValues::callProductService);
        ScopedValue.where(AUTH_TOKENS, "inventory-service-token")
                .run(DemonstrateContextHandlingWithScopedValues::callInventoryService);

        log.info("Order service completed with token: {}", AUTH_TOKENS.get());
    }

    // This is a client to call product service
    private static void callProductService() {
        log.info("calling product-service with header. Authorization: Bearer {}", AUTH_TOKENS.get());
    }

    // This is a client to call inventory service
    private static void callInventoryService() {
        log.info("calling inventory-service with header. Authorization: Bearer {}", AUTH_TOKENS.get());
    }
}
