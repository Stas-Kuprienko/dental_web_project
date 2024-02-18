package edu.dental.service.lifecycle;

import edu.dental.domain.APIManager;
import stas.utilities.LoggerKit;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

public enum LifecycleMonitor {

    INSTANCE;

    private final LoggerKit loggerKit;

    public final long lifecycle;
    private final ScheduledExecutorService scheduleService;

    LifecycleMonitor() {
        this.scheduleService = Executors.newSingleThreadScheduledExecutor();
        this.lifecycle = TimeUnit.MINUTES.toMillis(30);
        this.loggerKit = new LoggerKit(APIManager.getFileHandler());
        loggerKit.addLogger(this.getClass());
    }

    public void revision(ConcurrentHashMap<Integer, ? extends Monitorable> map) {
        Inspector<? extends Monitorable> inspector = new Inspector<>(map);
        scheduleService.schedule(inspector, lifecycle, TimeUnit.MILLISECONDS);

        String message = "revision is started";
        loggerKit.doLog(this.getClass(), message, Level.INFO);
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
            String message = "revision is executed";
            loggerKit.doLog(this.getClass(), message, Level.INFO);
            
            revision(map);
        }
    }
}