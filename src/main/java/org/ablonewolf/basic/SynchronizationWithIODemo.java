package org.ablonewolf.basic;

import org.ablonewolf.util.ThreadUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class SynchronizationWithIODemo {

    private static final Logger logger = LoggerFactory.getLogger(SynchronizationWithIODemo.class);
    private static final List<Integer> numbers = new ArrayList<>();

    static {
        System.setProperty("jdk.tracePinnedThreads", "full");
    }

    void main() {
        var virtualThreadBuilder = Thread.ofVirtual().name("VirtualThread - ", 1);
        executeIOTask(virtualThreadBuilder);

        ThreadUtils.sleep(Duration.ofSeconds(15));
    }

    private static void executeIOTask(Thread.Builder threadBuilder) {
        for (int i = 1; i < 50; i++) {
            threadBuilder.start(() -> {
                logger.info("Task started, thread name: {}", Thread.currentThread().getName());
                ioTask();
                logger.info("Task finished, thread name: {}", Thread.currentThread().getName());
            });
        }
    }

    private static synchronized void ioTask() {
        numbers.add(1);
        ThreadUtils.sleep(Duration.ofSeconds(10));
    }
}
