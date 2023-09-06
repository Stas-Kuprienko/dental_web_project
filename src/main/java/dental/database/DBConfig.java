package dental.database;

import java.io.*;
import java.util.Properties;

public final class DBConfig {

    private DBConfig() {}

    public static final String DB_URL = "db.url";
    public static final String DB_LOGIN = "db.login";
    public static final String DB_PASSWORD = "db.password";

    /**
     * The properties file with the url, user and password values.
     */
    private static final Properties prop = new Properties();
    public static final String PROPERTIES_FILE = "connect.properties";

    /**
     * To get the properties value by key string.
     * @param name The property key.
     * @return The value in this property list with the specified key value.
     */
    public static String getProp(String name) {
        if (prop.isEmpty()) {
            try (InputStream input = DBConfig.class.getClassLoader().getResourceAsStream(PROPERTIES_FILE)) {
                prop.load(input);
            } catch (IOException e) {
                //TODO to make loggers
                e.printStackTrace();
                throw new RuntimeException();
            }
        }
        return prop.getProperty(name);
    }

}
