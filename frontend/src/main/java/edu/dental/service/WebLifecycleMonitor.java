package edu.dental.service;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public enum WebLifecycleMonitor {

    INSTANCE;

    public static final long lifecycle = TimeUnit.MINUTES.toMillis(30);
    private final ScheduledExecutorService scheduleService;

    WebLifecycleMonitor() {
        this.scheduleService = Executors.newSingleThreadScheduledExecutor();
    }

    public void revision(ConcurrentHashMap <Integer, ? extends Monitorable> map) {
        Inspector<? extends Monitorable> inspector = new Inspector<>(map);
        scheduleService.schedule(inspector, lifecycle, TimeUnit.MILLISECONDS);
    }

    public void shutdown() {
        scheduleService.shutdown();
    }

    private class Inspector<T extends Monitorable> implements Runnable {

        private final ConcurrentHashMap<Integer, T> map;

        private Inspector(ConcurrentHashMap<Integer, T> map) {
            this.map = map;
        }

        @Override
        public void run() {
            for (T t : map.values()) {
                if ((System.currentTimeMillis() - t.lastAction()) > lifecycle) {
                    map.remove(t.getKey());
                }
            }
            revision(map);
        }
    }
}
