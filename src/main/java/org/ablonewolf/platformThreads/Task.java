package org.ablonewolf.platformThreads;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;

public class Task {
    private static final Logger logger = LoggerFactory.getLogger(Task.class);

    public static void doBlockingTask(Integer i) {
        try {
            logger.info("Blocking task {} started", i);
            Thread.sleep(Duration.ofSeconds(10));
            logger.info("Blocking task {} finished", i);
        } catch (InterruptedException e) {
            logger.error("Task {} interrupted, details: {}", i, e.getMessage());
        }
    }
}
