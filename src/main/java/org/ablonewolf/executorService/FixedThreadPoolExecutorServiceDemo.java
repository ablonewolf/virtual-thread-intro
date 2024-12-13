package org.ablonewolf.executorService;

import org.ablonewolf.util.CommonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Executors;

public class FixedThreadPoolExecutorServiceDemo {

    private static final Logger logger = LoggerFactory.getLogger(FixedThreadPoolExecutorServiceDemo.class);

    void main() {
        CommonUtils.executeByExecutorService(Executors.newFixedThreadPool(5), 20, logger);
    }
}
