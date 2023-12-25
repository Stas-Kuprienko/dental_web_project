package edu.dental;

import edu.dental.service.JsonObjectParser;
import edu.dental.service.Repository;

import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Properties;

public enum WebAPI {

    INSTANCE;

    public final String paramToken = "token";

    private static final String PROP_PATH = "D:\\Development Java\\pet_projects\\dental_web_project\\backend\\core\\target\\classes\\service.properties";

    private final Properties service;

    private final Repository repository;
    private final JsonObjectParser jsonObjectParser;


    WebAPI() {
        service = new Properties();
        try (FileInputStream fileInput = new FileInputStream(PROP_PATH)) {
            service.load(fileInput);
        } catch (IOException e) {
            //TODO loggers
            throw new RuntimeException(e);
        }
        this.repository = init(Repository.class);
        this.jsonObjectParser = init(JsonObjectParser.class);
    }


    public Repository getRepository() {
        return repository;
    }

    public JsonObjectParser getJsonParser() {
        return jsonObjectParser;
    }

    private <T> String getClassName(Class<T> clas) {
        return service.getProperty(clas.getSimpleName());
    }

    @SuppressWarnings("unchecked")
    private <T> T init(Class<T> clas) {
        Constructor<?> constructor = null;
        try {
            Class<?> c = Class.forName(getClassName(clas));
            constructor = c.getDeclaredConstructor();
            constructor.setAccessible(true);
            return (T) constructor.newInstance();
        } catch (InvocationTargetException | NoSuchMethodException | InstantiationException
                 | IllegalAccessException | ClassNotFoundException e) {
            //TODO logger
            throw new RuntimeException(e);
        } finally {
            if (constructor != null) {
                constructor.setAccessible(false);
            }
        }
    }
}
