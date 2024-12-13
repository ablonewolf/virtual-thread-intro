package org.ablonewolf.util;

public final class CommonUtils {
    private CommonUtils() {
    }

    public static Long timer(Runnable runnable) {
        var startTime = System.currentTimeMillis();
        runnable.run();
        var endTime = System.currentTimeMillis();
        return endTime - startTime;
    }
}
