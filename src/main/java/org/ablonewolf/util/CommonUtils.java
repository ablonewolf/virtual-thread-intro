package org.ablonewolf.util;

import org.slf4j.Logger;

import java.time.Duration;
import java.util.concurrent.ExecutorService;

public final class CommonUtils {

    private CommonUtils() {
    }

    public static Long timer(Runnable runnable) {
        var startTime = System.currentTimeMillis();
        runnable.run();
        var endTime = System.currentTimeMillis();
        return endTime - startTime;
    }

    public static void executeByExecutorService(ExecutorService executorService, int taskCount, Logger logger) {
        try (executorService) {
            for (int i = 1; i <= taskCount; i++) {
                int taskNumber = i;
                executorService.submit(() -> ThreadUtils.executeIOTask(taskNumber, Duration.ofSeconds(5), logger));
                logger.info("Task {} submitted", taskNumber);
            }
        }
    }
}
