package org.ablonewolf.scopedValues;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Demonstrates the usage of Scoped Values in Java, how to access them within a specific scope, and what happens
 * when trying to access them outside that scope.
 */
public class DemonstrateScopedValues {

    private static final Logger log = LoggerFactory.getLogger(DemonstrateScopedValues.class);
    private static final ScopedValue<String> SESSION_TOKEN = ScopedValue.newInstance();

    static void main() {
        ScopedValue.where(SESSION_TOKEN, "session-1")
                .run(DemonstrateScopedValues::checkBinding);

        // this will print empty token since the scoped value is not bound here
        checkBinding();
    }

    // check if the scoped value is bound and print its value
    private static void checkBinding() {
        log.info("Is Token Bound: {}", SESSION_TOKEN.isBound());
        log.info("Token Value: {}", SESSION_TOKEN.orElse("No Token"));
    }
}
