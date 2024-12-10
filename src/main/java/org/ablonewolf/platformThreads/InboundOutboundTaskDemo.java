package org.ablonewolf.platformThreads;

public class InboundOutboundTaskDemo {

    private static final Integer MAX_PLATFORM_THREADS = 60000;

    public static void main() {
        platformThreadsDemo();
    }

    private static void platformThreadsDemo() {
        var threadBuilder = Thread.ofPlatform().name("PlatformThread - ", 1);
        for (int i = 0; i < MAX_PLATFORM_THREADS; i++) {
            final Integer taskNumber = i;
            /*
             this is not the ideal way to create threads in a production environment
             since we are exploring a concept, we are sticking to this approach for the time being
             due to the high value store in MAX_PLATFORM_THREADS, this code won't create that many threads
            */
            Thread thread = threadBuilder.unstarted(() -> Task.doBlockingTask(taskNumber));
            thread.start();
        }
    }
}
