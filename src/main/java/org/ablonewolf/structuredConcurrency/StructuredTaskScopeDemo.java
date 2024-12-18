package org.ablonewolf.structuredConcurrency;

import org.ablonewolf.util.ThreadUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.concurrent.StructuredTaskScope;
import java.util.concurrent.ThreadLocalRandom;

public class StructuredTaskScopeDemo {

    private static final Logger log = LoggerFactory.getLogger(StructuredTaskScopeDemo.class);

    void main() {
        var virtualThreadFactory = Thread.ofVirtual().name("Virtual Thread - ", 1).factory();
        try (var taskScope = new StructuredTaskScope<>(null, virtualThreadFactory)) {
            var firstSubTask = taskScope.fork(StructuredTaskScopeDemo::getDeltaFlightNumber);
            var secondSubTask = taskScope.fork(StructuredTaskScopeDemo::getFrontierFlightNumber);

            taskScope.join();

            // checking the status
            log.info("first SubTask state = {}", firstSubTask.state());
            log.info("second SubTask state = {}", secondSubTask.state());

            // printing the result
            log.info("first SubTask result = {}", firstSubTask.get());
            log.info("second SubTask result = {}", secondSubTask.get());
        } catch (InterruptedException e) {
            log.error("TaskScope join was interrupted, details: {}", e.getMessage());
        }
    }

    private static String getDeltaFlightNumber() {
        var random = ThreadLocalRandom.current().nextInt(100, 1000);
        log.info("delta flight number: {}", random);
        ThreadUtils.sleep("Delta", Duration.ofSeconds(1));
        return STR."Delta-$\{random}";
    }

    private static String getFrontierFlightNumber() {
        var random = ThreadLocalRandom.current().nextInt(100, 1000);
        log.info("frontier flight number: {}", random);
        ThreadUtils.sleep("Frontier", Duration.ofSeconds(3));
        return STR."Frontier-$\{random}";
    }
}
