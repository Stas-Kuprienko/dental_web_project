package edu.dental.database.mysql_api.dao;

import edu.dental.database.dao.DAO;

public enum MySqlSamples implements DAO.Queries {

    INSERT("INSERT INTO %s (%s) VALUES (%s);"),

    INSERT_BATCH("INSERT INTO %s VALUES (%s);"),

    SELECT_ALL("SELECT %s FROM %s;"),

    SELECT_WHERE("SELECT %s FROM %s WHERE %s;"),

    DELETE("DELETE FROM %s WHERE %s;"),

    //TODO
    UPDATE("UPDATE %s SET %s WHERE %s;"),

    SELECT_WORK_RECORD("""
            SELECT work_record.*,
            	GROUP_CONCAT(product.title) AS product,
                GROUP_CONCAT(product.quantity) AS quantity,
                GROUP_CONCAT(product.price) AS price
            	FROM work_record
                JOIN product ON product.work_id = work_record.id
            	WHERE %s
                GROUP BY work_record.id;
            """);

    public final String QUERY;

    MySqlSamples(String QUERY) {
        this.QUERY = QUERY;
    }

}
