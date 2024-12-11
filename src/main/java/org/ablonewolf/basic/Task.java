package org.ablonewolf.basic;

import org.ablonewolf.commonUtils.ThreadUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;

public class Task {
    private static final Logger logger = LoggerFactory.getLogger(Task.class);

    public static void doBlockingTask(Integer i) {
        logger.info("Blocking task {} started", i);
        ThreadUtils.sleep(Duration.ofSeconds(1));
        logger.info("Blocking task {} finished", i);
    }

    public static Long findFibonacci(Long number) {
        if (number < 2) {
            return number;
        }
        return findFibonacci(number - 1) + findFibonacci(number - 2);
    }
}
