package edu.dental.service;

import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LoggerKit {

    private final ConcurrentHashMap<String, Logger> loggerMap;

    public LoggerKit() {
        this.loggerMap = new ConcurrentHashMap<>();
        Logger logger = Logger.getLogger(this.getClass().getName());
        loggerMap.put(this.getClass().getSimpleName(), logger);
    }

    public void addLogger(Class<?> clas) {
        Logger logger = Logger.getLogger(clas.getName());
        logger.addHandler(WebAPIManager.fileHandler);
        logger.setLevel(Level.ALL);
        loggerMap.put(clas.getSimpleName(), logger);
    }

    public void doLog(Class<?> clas, Exception e, Level level) throws NullPointerException {
        Logger logger = loggerMap.get(clas.getSimpleName());
        if (logger == null) {
            NullPointerException nullPointerException = new NullPointerException("logger is not found");
            doLog(this.getClass(), nullPointerException, Level.SEVERE);
            throw nullPointerException;
        } else {
            logger.log(level, buildStackTraceMessage(e.getStackTrace()));
        }
    }

    public void doLog(Class<?> clas, String message, Level level) throws NullPointerException {
        Logger logger = loggerMap.get(clas.getSimpleName());
        if (logger == null) {
            NullPointerException nullPointerException = new NullPointerException("logger is not found");
            doLog(this.getClass(), nullPointerException, Level.SEVERE);
            throw nullPointerException;
        } else {
            logger.log(level, message);
        }
    }

    public static String buildStackTraceMessage(StackTraceElement[] stackTrace) {
        StringBuilder str = new StringBuilder();
        for (StackTraceElement e : stackTrace) {
            str.append(e.toString()).append("\n");
        }
        return str.toString();
    }
}
