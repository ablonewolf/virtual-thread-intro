package org.ablonewolf.basic;

import org.ablonewolf.util.ThreadUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.concurrent.ThreadFactory;

public class ThreadFactoryDemo {

    private static final Logger logger = LoggerFactory.getLogger(ThreadFactoryDemo.class);

    void main() {
        ThreadFactory virtualThreadFactory = Thread.ofVirtual().name("Virtual Thread - ", 1).factory();
        demonstrateThreadFactory(virtualThreadFactory);
        ThreadUtils.sleep(Duration.ofSeconds(10));
    }

    private static void demonstrateThreadFactory(ThreadFactory threadFactory) {
        for (int i = 0; i < 30; i++) {
            var parentThread = threadFactory.newThread(() -> {
                logger.info("Task started, thread name: {}", Thread.currentThread().getName());
                var childThread = threadFactory.newThread(() -> {
                    logger.info("Child task started, thread name: {}", Thread.currentThread().getName());
                    ThreadUtils.sleep(Duration.ofSeconds(3));
                    logger.info("Child task finished, thread name: {}", Thread.currentThread().getName());
                });
                childThread.start();
                logger.info("Task finished, thread name: {}", Thread.currentThread().getName());
            });

            parentThread.start();
        }
    }
}
