package org.ablonewolf.basic;

import org.ablonewolf.util.ThreadUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;

public final class CooperativeSchedulingDemo {

    private static final Logger logger = LoggerFactory.getLogger(CooperativeSchedulingDemo.class);

    static {
        System.setProperty("jdk.virtualThreadScheduler.parallelism", "1");
        System.setProperty("jdk.virtualThreadScheduler.maxPoolSize", "1");
    }

    void main() {
        var virtualThreadBuilder = Thread.ofVirtual().name("VirtualThread");
        var firstThread = virtualThreadBuilder.unstarted(() -> DemoExecution(1));
        var secondThread = virtualThreadBuilder.unstarted(() -> DemoExecution(2));

        firstThread.start();
        secondThread.start();

        // sleeping the main thread to make the virtual threads execute as they are daemon threads by default
        ThreadUtils.sleep(Duration.ofSeconds(3));
    }


    private static void DemoExecution(int threadNumber) {
        logger.info("Thread-{} started execution", threadNumber);

        for (int i = 1; i <= 10; i++) {
            logger.info("Thread-{} is printing {}", threadNumber, i);
            Thread.yield();
        }

        logger.info("Thread-{} finished execution", threadNumber);
    }
}
