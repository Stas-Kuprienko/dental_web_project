package edu.dental.database.mysql_api.dao;

import edu.dental.database.dao.DAO;

public enum MySqlSamples implements DAO.Queries {

    INSERT("INSERT INTO %s (%s) VALUES (%s);"),

    INSERT_BATCH("INSERT INTO %s VALUES (%s);"),

    SELECT_ALL("SELECT %s FROM %s;"),

    SELECT_WHERE("SELECT %s FROM %s WHERE %s;"),

    DELETE("DELETE FROM %s WHERE %s;"),

    //TODO
    UPDATE("UPDATE %s SET %s WHERE %s;");

    public final String QUERY;

    MySqlSamples(String QUERY) {
        this.QUERY = QUERY;
    }

}
