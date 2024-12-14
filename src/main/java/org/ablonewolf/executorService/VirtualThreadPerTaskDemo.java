package org.ablonewolf.executorService;

import org.ablonewolf.util.CommonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Executors;

public class VirtualThreadPerTaskDemo {
    private static final Logger logger = LoggerFactory.getLogger(VirtualThreadPerTaskDemo.class);

    void main() {
        CommonUtils.executeByExecutorService(Executors.newVirtualThreadPerTaskExecutor(), 200, logger);
    }
}
