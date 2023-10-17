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
            	GROUP_CONCAT(product.title) AS entry_id,
                GROUP_CONCAT(product_map.title) AS title,
                GROUP_CONCAT(product.quantity) AS quantity,
                GROUP_CONCAT(product.price) AS price
            	FROM work_record
                JOIN product ON product.work_id = work_record.id
                JOIN product_map ON product_map.id = product.title
            	WHERE %s
                GROUP BY work_record.id;
            """),

    SELECT_PRODUCT("""
            SELECT product.title AS entry_id,
            product_map.title AS title, product.quantity, product.price
            FROM product
            JOIN product_map ON product_map.id = product.title
            WHERE product.title IN
            (SELECT product_map.id FROM product_map WHERE product_map.title = ?)
            AND
            (work_id IN
            (SELECT work_record.id FROM work_record WHERE work_record.user_id =
            (SELECT work_record.user_id FROM work_record WHERE work_record.id = ?)))
            AND quantity = ?;
            """);

    public final String QUERY;

    MySqlSamples(String QUERY) {
        this.QUERY = QUERY;
    }

}
