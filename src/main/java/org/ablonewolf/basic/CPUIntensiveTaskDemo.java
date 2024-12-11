package org.ablonewolf.basic;

import org.ablonewolf.util.CommonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CountDownLatch;

/**
 * Comparing the performance over a CPU intensive operation between platform threads and virtual threads
 */
public class CPUIntensiveTaskDemo {

    private static final Logger logger = LoggerFactory.getLogger(CPUIntensiveTaskDemo.class);
    private static final int THREAD_COUNT = 3 * Runtime.getRuntime().availableProcessors();

    void main() {
        /*
          estimating the performance for the platform threads for a CPU intensive operation
         */
        var totalTimeTakenForPlatformThreads = CommonUtils.timer(() -> {
            logger.info("Starting platform threads for CPU intensive task");
            startCPUIntensiveTask(Thread.ofPlatform().name("Platform - ", 1));
            logger.info("All platform threads finished execution.");
        });
        logger.info("Total time taken for platform threads to finish: {} ms", totalTimeTakenForPlatformThreads);

        /*
          estimating the performance for the virtual threads for a CPU intensive operation
         */
        var totalTimeTakenForVirtualThreads = CommonUtils.timer(() -> {
            logger.info("Starting virtual threads for CPU intensive task");
            startCPUIntensiveTask(Thread.ofVirtual().name("Virtual - ", 1));
            logger.info("All virtual threads finished execution.");
        });
        logger.info("Total time taken for virtual threads to finish: {} ms", totalTimeTakenForVirtualThreads);
    }

    private static void startCPUIntensiveTask(Thread.Builder builder) {
        var latch = new CountDownLatch(THREAD_COUNT);
        for (int i = 1; i <= THREAD_COUNT; i++) {
            builder.start(() -> {
                doCPUIntensiveTask(45);
                latch.countDown();
            });
        }
        try {
            latch.await();
        } catch (InterruptedException e) {
            logger.error("Count down latch was interrupted, details: {}", e.getMessage());
        }
    }

    private static void doCPUIntensiveTask(int i) {
        var currentThread = Thread.currentThread();
        CommonUtils.timer(() -> {
            var fibonacciNumber = Task.findFibonacci((long) i);
            logger.info("The {}th fibonacci number is: {}, calculated in Thread: {}", i, fibonacciNumber,
                currentThread.getName());
        });
    }
}
