package org.ablonewolf.executorService;

import org.ablonewolf.util.ThreadUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.concurrent.Executors;

/**
 * in Java 21, Executor service extends the AutoCloseable interface
 */
public class AutoCloseableDemo {

    private static final Logger logger = LoggerFactory.getLogger(AutoCloseableDemo.class);

    void main() {
        try (var executorService = Executors.newSingleThreadExecutor()) {
            executorService.submit(AutoCloseableDemo::task);
            logger.info("Task submitted");
        }
    }

    private static void task() {
        logger.info("Task started");
        ThreadUtils.sleep(Duration.ofSeconds(1));
        logger.info("Task finished");
    }
}
