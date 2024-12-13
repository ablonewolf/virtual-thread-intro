package org.ablonewolf.executorService;

import org.ablonewolf.util.CommonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Executors;

/**
 * Demonstrating how Single Thread Executor Service Works
 */
public class SingleThreadExecutorServiceDemo {

    private static final Logger logger = LoggerFactory.getLogger(SingleThreadExecutorServiceDemo.class);

    void main() {
        CommonUtils.executeByExecutorService(Executors.newSingleThreadExecutor(), 3, logger);
    }

}
