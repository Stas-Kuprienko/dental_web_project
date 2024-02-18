package edu.dental.database.connection;

import java.util.Properties;

public final class DBConfiguration {

    private static final DBConfiguration config;
    static {
        config = new DBConfiguration();
    }

    private DBConfiguration() {}


    public static final String URL = "db.url";
    public static final String LOGIN = "db.login";
    public static final String PASSWORD = "db.password";
    public static final String DRIVER = "driver";

    public static final String DATA_BASE = "dental";

    /**
     * The {@link Properties} file with the url, user and password values.
     */
    private Properties sqlProp;

    /**
     * To get the properties value by key string.
     * @param key The property key.
     * @return The value in this property list with the specified key value.
     */
    public String getProp(String key) {
        return sqlProp.getProperty(key);
    }

    public Properties getSqlProp() {
        return sqlProp;
    }

    public void setSqlProp(Properties sqlProp) {
        this.sqlProp = sqlProp;
    }

    public static synchronized DBConfiguration get() {
        return config;
    }
}