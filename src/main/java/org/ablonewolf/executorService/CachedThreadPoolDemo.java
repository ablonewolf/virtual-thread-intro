package org.ablonewolf.executorService;

import org.ablonewolf.util.CommonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Executors;

public class CachedThreadPoolDemo {

    private static final Logger logger = LoggerFactory.getLogger(CachedThreadPoolDemo.class);

    void main() {
        CommonUtils.executeByExecutorService(Executors.newCachedThreadPool(), 200, logger);
    }
}
