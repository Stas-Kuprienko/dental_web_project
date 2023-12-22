package edu.dental.database.connection;

import java.io.*;
import java.util.Properties;

public final class DBConfiguration {

    private static final DBConfiguration config;
    static {
        config = new DBConfiguration();
    }

    private DBConfiguration() {
        sqlProp = new Properties();
        try (FileInputStream fileInput = new FileInputStream(PROP_PATH)) {
            sqlProp.load(fileInput);
        } catch (IOException e) {
            //TODO loggers
            throw new RuntimeException(e);
        }
    }


    public static final String URL = "db.url";
    public static final String LOGIN = "db.login";
    public static final String PASSWORD = "db.password";
    public static final String DRIVER = "driver";

    public static final String DATA_BASE = "dental";

    /**
     * The {@link Properties} file with the url, user and password values.
     */
    private final Properties sqlProp;

    /**
     * The path to the {@link Properties} file for connecting to a database.
     */
    private static final String PROP_PATH = "D:\\Development Java\\pet_projects\\dental_web_project\\backend\\core\\target\\classes\\mysql.properties";

    /**
     * To get the properties value by key string.
     * @param key The property key.
     * @return The value in this property list with the specified key value.
     */
    public String getProp(String key) {
        return sqlProp.getProperty(key);
    }

    public static synchronized DBConfiguration get() {
        return config;
    }
}