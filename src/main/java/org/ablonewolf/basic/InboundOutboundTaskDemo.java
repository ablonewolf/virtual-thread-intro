package org.ablonewolf.basic;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CountDownLatch;

public class InboundOutboundTaskDemo {

    private static final Integer MAX_PLATFORM_THREADS = 200;
    private static final Integer MAX_VIRTUAL_THREADS = 50000;
    private static final Logger logger = LoggerFactory.getLogger(InboundOutboundTaskDemo.class);

    public static void main() {
        platformThreadsDemo();
        virtualThreadsDemo();
    }

    private static void platformThreadsDemo() {
        var threadBuilder = Thread.ofPlatform().name("PlatformThread - ", 1);
        for (int i = 0; i < MAX_PLATFORM_THREADS; i++) {
            final Integer taskNumber = i;
            /*
             this is not the ideal way to create threads in a production environment
             since we are exploring a concept, we are sticking to this approach for the time being
             due to the high value store in MAX_PLATFORM_THREADS, this code won't create that many threads
            */
            Thread thread = threadBuilder.unstarted(() -> Task.doBlockingTask(taskNumber));
            thread.start();
        }
    }

    /**
     * demonstrate how to start a virtual thread and how to use the latch to make it await
     */
    private static void virtualThreadsDemo() {
        /*
         * since virtual threads are daemon threads by default, we need to use countdown latch to make them wait
         * in order to simulate the blocking scenario
         */
        CountDownLatch latch = new CountDownLatch(MAX_VIRTUAL_THREADS);
        var virtualThreadBuilder = Thread.ofVirtual().name("VirtualThread - ", 1);
        for (int i = 0; i < MAX_VIRTUAL_THREADS; i++) {
            final Integer taskNumber = i;
            Thread thread = virtualThreadBuilder.unstarted(() -> {
                try {
                    Task.doBlockingTask(taskNumber);
                } finally {
                    latch.countDown(); // Decrement the latch
                }
            });
            thread.start();
        }
        try {
            latch.await(); // Wait until all threads finish
        } catch (InterruptedException e) {
            logger.error("Count down latch was interrupted, details: {}", e.getMessage());
        }
    }

}
