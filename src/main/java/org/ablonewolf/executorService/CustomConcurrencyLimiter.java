package org.ablonewolf.executorService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Semaphore;

public class CustomConcurrencyLimiter implements AutoCloseable {

    private static final Logger logger = LoggerFactory.getLogger(CustomConcurrencyLimiter.class);

    private final ExecutorService executor;
    private final Semaphore semaphore;

    public CustomConcurrencyLimiter(ExecutorService executor, Integer limit) {
        this.executor = executor;
        this.semaphore = new Semaphore(limit);
    }

    public <T> void submit(Callable<T> task) {
        executor.submit(() -> wrapCallableWithSemaphore(task));
    }

    private <T> T wrapCallableWithSemaphore(Callable<T> callable) {
        try {
            semaphore.acquire();
            return callable.call();
        } catch (Exception e) {
            logger.error("An error occurred while wrapping the callable, details: {}", e.getMessage());
        } finally {
            semaphore.release();
        }
        return null;
    }

    @Override
    public void close() {
        this.executor.close();
    }
}
