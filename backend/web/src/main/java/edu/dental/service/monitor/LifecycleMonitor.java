package edu.dental.service.monitor;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public enum LifecycleMonitor {

    INSTANCE;

    public final long lifecycle;
    private final ScheduledExecutorService scheduleService;

    LifecycleMonitor() {
        this.scheduleService = Executors.newSingleThreadScheduledExecutor();
        this.lifecycle = TimeUnit.MINUTES.toMillis(30);
    }

    public void revision(ConcurrentHashMap<Integer, ? extends Monitorable> map) {
        Inspector<? extends Monitorable> inspector = new Inspector<>(map);
        scheduleService.schedule(inspector, lifecycle, TimeUnit.MILLISECONDS);
    }

    public void shutdown() {
        scheduleService.shutdown();
    }


    private class Inspector <T extends Monitorable> implements Runnable {

        private final ConcurrentHashMap<Integer, T> map;

        private Inspector(ConcurrentHashMap<Integer, T> map) {
            this.map = map;
        }

        @Override
        public void run() {
            for (T a : map.values()) {
                if ((System.currentTimeMillis() - a.lastAction()) > lifecycle) {
                    map.remove(a.getKey());
                }
            }
            revision(map);
        }
    }
}