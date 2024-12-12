package org.ablonewolf.basic;

import org.ablonewolf.util.ThreadUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class RaceConditionDemo {

    private static final Logger logger = LoggerFactory.getLogger(RaceConditionDemo.class);
    private static final List<Integer> numbers = new ArrayList<>();
    private static final List<Integer> synchronizedNumbers = new ArrayList<>();

    void main() {

        processTask(Thread.ofVirtual().name("Virtual Thread - ", 1));

        ThreadUtils.sleep(Duration.ofSeconds(2));

        logger.info("List size: {}", numbers.size());
        logger.info("Synchronized list size: {}", synchronizedNumbers.size());
    }

    private static void processTask(Thread.Builder threadBuilder) {
        for (int i = 0; i < 50; i++) {
            threadBuilder.start(() -> {
                var threadName = Thread.currentThread().getName();
                logger.info("Task started in thread {}", threadName);
                for (int j = 0; j < 200; j++) {
                    increaseListSize();
                    increaseListSizeSynchronized();
                }
                logger.info("Task finished in thread {}", threadName);
            });
        }
    }

    private static void increaseListSize() {
        numbers.add(1);
    }

    private static synchronized void increaseListSizeSynchronized() {
        synchronizedNumbers.add(1);
    }
}
