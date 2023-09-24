package edu.dental.database.connection;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public final class DBConfiguration {

    private DBConfiguration() {}

    static {
        prop = new Properties();
    }

    public static final String DB_URL = "db.url";
    public static final String DB_LOGIN = "db.login";
    public static final String DB_PASSWORD = "db.password";

    public static final String DATA_BASE = "mydb";

    /**
     * The {@link Properties} file with the url, user and password values.
     */
    private static final Properties prop;

    /**
     * The path to the {@link Properties} file for connecting to a database.
     */
    public static final String PROPERTIES_FILE = "mysql.properties";

    /**
     * To get the properties value by key string.
     * @param name The property key.
     * @return The value in this property list with the specified key value.
     */
    public synchronized static String getProp(String name) {
        if (prop.isEmpty()) {
            try (InputStream input = DBConfiguration.class.getClassLoader().getResourceAsStream(PROPERTIES_FILE)) {
                prop.load(input);
            } catch (IOException e) {
                //TODO to make loggers
                throw new RuntimeException(e);
            }
        }
        return prop.getProperty(name);
    }
}