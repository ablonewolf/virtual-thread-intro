package org.ablonewolf.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;

public final class ThreadUtils {

    private ThreadUtils() {
    }

    private static final Logger logger = LoggerFactory.getLogger(ThreadUtils.class);

    public static void sleep(Duration duration) {
        try {
            Thread.sleep(duration.toMillis());
        } catch (InterruptedException e) {
            logger.error("Thread sleep was interrupted, details: {}", e.getMessage());
        }
    }

    public static void executeIOTask(int taskNumber, Duration duration, Logger logger) {
        var threadInfo = Thread.currentThread();
        logger.info("Task {} started, thread info: {}", taskNumber, threadInfo);
        ThreadUtils.sleep(duration);
        logger.info("Task {} finished, thread info: {}", taskNumber, threadInfo);
    }
}
