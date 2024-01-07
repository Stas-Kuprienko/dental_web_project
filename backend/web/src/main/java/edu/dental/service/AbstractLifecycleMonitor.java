package edu.dental.service;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public abstract class AbstractLifecycleMonitor {
//TODO

    public final long lifecycle;
    protected static final ScheduledExecutorService scheduleService;

    static {
        scheduleService = Executors.newSingleThreadScheduledExecutor();
    }
    public AbstractLifecycleMonitor(int minute) {
        this.lifecycle = TimeUnit.MINUTES.toMillis(minute);
    }

    public abstract void revision();

    public static void shutdown() {
        scheduleService.shutdown();
    }
}
