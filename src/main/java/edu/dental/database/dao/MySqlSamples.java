package edu.dental.database.dao;

public enum MySqlSamples implements DAO.Queries {

    INSERT("INSERT INTO %s (%s) VALUES (%s);"),

    SELECT_ALL("SELECT %s FROM %s;"),

    SELECT_WHERE("SELECT %s FROM %s WHERE %s;"),

    DELETE("DELETE FROM %s WHERE %s;"),

    //TODO
    UPDATE("UPDATE %s SET %s = %s WHERE %s;");

    public final String QUERY;

    MySqlSamples(String QUERY) {
        this.QUERY = QUERY;
    }

}
