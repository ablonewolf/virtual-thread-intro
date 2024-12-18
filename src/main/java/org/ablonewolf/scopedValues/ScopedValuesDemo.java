package org.ablonewolf.scopedValues;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;

public class ScopedValuesDemo {

    private static final Logger log = LoggerFactory.getLogger(ScopedValuesDemo.class);
    private static final ScopedValue<String> SESSION_TOKEN = ScopedValue.newInstance();

    void main() {
        Thread.ofPlatform().start(ScopedValuesDemo::processIncomingRequests);
        Thread.ofPlatform().start(ScopedValuesDemo::processIncomingRequests);
    }

    private static void processIncomingRequests() {
        var token = authenticate();
        ScopedValue.runWhere(SESSION_TOKEN, token, ScopedValuesDemo::controller);
    }

    private static String authenticate() {
        var token = UUID.randomUUID().toString();
        log.info("token: {}", token);
        return token;
    }

    private static void controller() {
        var token = SESSION_TOKEN.orElse("No token");
        log.info("Controller received token: {}", token);
        ScopedValue.runWhere(SESSION_TOKEN, token, ScopedValuesDemo::service);
    }

    private static void service() {
        var token = SESSION_TOKEN.orElse("No token");
        log.info("Service received token: {}", token);
        ScopedValue.runWhere(SESSION_TOKEN, token, ScopedValuesDemo::callExternalService);
    }

    private static void callExternalService() {
        log.info("Making an HTTP request to the external service with this token: {}",
            SESSION_TOKEN.orElse("No token"));
    }
}
