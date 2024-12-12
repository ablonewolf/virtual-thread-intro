package org.ablonewolf.basic;

import org.ablonewolf.util.ThreadUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ReentrantLockWithIOTaskDemo {

    private static final Logger logger = LoggerFactory.getLogger(ReentrantLockWithIOTaskDemo.class);
    private static final Lock reentrantLock = new ReentrantLock();

    void main() {

        processBlockingTask(Thread.ofVirtual().name("VirtualThread - ", 1));

        ThreadUtils.sleep(Duration.ofSeconds(2));
    }


    private static void processBlockingTask(Thread.Builder threadBuilder) {
        for (int i = 1; i <= 50; i++) {
            threadBuilder.start(() -> {
                var threadName = Thread.currentThread().getName();
                logger.info("Task started, thread name: {}", threadName);
                ioTaskWithSynchronization();
            });
        }
    }

    private static void ioTaskWithSynchronization() {
        try {
            reentrantLock.lock();
            ThreadUtils.sleep(Duration.ofSeconds(10));
        } catch (Exception e) {
            logger.error("Exception occurred, details : {}", e.getMessage());
        } finally {
            reentrantLock.unlock();
        }
    }
}
