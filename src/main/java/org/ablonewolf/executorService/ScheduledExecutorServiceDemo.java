package org.ablonewolf.executorService;

import org.ablonewolf.util.ThreadUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ScheduledExecutorServiceDemo {

    private static final Logger logger = LoggerFactory.getLogger(ScheduledExecutorServiceDemo.class);
    private int taskId = 1;

    void main() {
        var virtualThreadFactory = Thread.ofVirtual().name("Virtual - ", 1).factory();
        var executorService = Executors.newThreadPerTaskExecutor(virtualThreadFactory);
        var scheduler = Executors.newSingleThreadScheduledExecutor();

        try (scheduler; executorService) {
            scheduler.scheduleAtFixedRate(
                () -> executorService.submit(() -> ThreadUtils.executeIOTask(taskId++, Duration.ofSeconds(1), logger)),
                0, 3, TimeUnit.SECONDS);

            ThreadUtils.sleep(Duration.ofSeconds(30));
        }
        executorService.shutdown();
        scheduler.shutdown();
    }
}

