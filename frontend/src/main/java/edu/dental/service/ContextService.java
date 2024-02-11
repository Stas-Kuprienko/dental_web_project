package edu.dental.service;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

@WebListener("/")
public class ContextService implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        WebAPIManager.INSTANCE.getLoggerKit();
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {

    }
}
