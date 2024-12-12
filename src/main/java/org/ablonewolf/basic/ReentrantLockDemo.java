package org.ablonewolf.basic;

import org.ablonewolf.util.ThreadUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ReentrantLockDemo {

    private static final Logger logger = LoggerFactory.getLogger(ReentrantLockDemo.class);
    private static final Lock reentrantLock = new ReentrantLock();
    private static final List<Integer> numbers = new ArrayList<>();

    void main() {
        processTask(Thread.ofVirtual().name("VirtualThread - ", 1));

        ThreadUtils.sleep(Duration.ofSeconds(2));

        logger.info("List size: {}", numbers.size());
    }

    private static void processTask(Thread.Builder threadBuilder) {
        for (int i = 1; i <= 50; i++) {
            threadBuilder.start(() -> {
                var threadName = Thread.currentThread().getName();
                logger.info("Task started, thread name: {}", threadName);
                for (int j = 1; j <= 200; j++) {
                    inMemoryTask();
                }
                logger.info("Task finished, thread name: {}", threadName);
            });
        }
    }

    private static void inMemoryTask() {
        try {
            reentrantLock.lock();
            numbers.add(1);
        } catch (Exception e) {
            logger.error("Exception occurred, details : {}", e.getMessage());
        } finally {
            reentrantLock.unlock();
        }
    }

}
