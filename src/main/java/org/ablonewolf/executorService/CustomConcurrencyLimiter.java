package org.ablonewolf.executorService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;
import java.util.Queue;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Semaphore;

public class CustomConcurrencyLimiter<T> implements AutoCloseable {

    private static final Logger logger = LoggerFactory.getLogger(CustomConcurrencyLimiter.class);

    private final ExecutorService executor;
    private final Semaphore semaphore;
    private final Queue<Callable<T>> queue;

    public CustomConcurrencyLimiter(ExecutorService executor, Integer limit) {
        this.executor = executor;
        this.semaphore = new Semaphore(limit);
        this.queue = new ConcurrentLinkedQueue<>();
    }

    public void submit(Callable<T> task) {
        this.queue.add(task);
        executor.submit(this::executeTaskWithSemaphore);
    }

    private T executeTaskWithSemaphore() {
        try {
            semaphore.acquire();
            var callable = queue.poll();
            if (Objects.nonNull(callable)) {
                return callable.call();
            }
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
