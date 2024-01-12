package edu.dental.database.mysql_api;

public enum MySqlSamples {

    INSERT("INSERT INTO %s (%s) VALUES (%s);"),

    INSERT_BATCH("INSERT INTO %s VALUES (%s);"),

    SELECT_ALL("SELECT %s FROM %s;"),

    SELECT_WHERE("SELECT %s FROM %s WHERE %s;"),

    DELETE("DELETE FROM %s WHERE %s;"),

    //TODO
    UPDATE("UPDATE %s SET %s WHERE %s;"),

    REPORT_ID("SELECT id FROM report WHERE year = %s AND month = '%s'"),

    SELECT_DENTAL_WORK("""
            SELECT dental_work.*,
            	GROUP_CONCAT(product.title) AS entry_id,
                GROUP_CONCAT(product_map.title) AS title,
                GROUP_CONCAT(product.quantity) AS quantity,
                GROUP_CONCAT(product.price) AS price
            	FROM dental_work
                LEFT JOIN product ON product.work_id = dental_work.id
                LEFT JOIN product_map ON product_map.id = product.title
            	WHERE %s
                GROUP BY dental_work.id;
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
                (SELECT dental_work.id FROM dental_work WHERE dental_work.user_id =
                (SELECT dental_work.user_id FROM dental_work WHERE dental_work.id = ?)))
                AND quantity = ?;
            """),

    ALL_PROFITS("""
            SELECT report.month,
                report.year,
                SUM(product.price * product.quantity) AS amount
            	FROM dental_work
                JOIN product ON product.work_id = dental_work.id
                JOIN report ON dental_work.report_id = report.id
            	WHERE dental_work.user_id = ?
                GROUP BY dental_work.report_id, report.month, report.year;
            """),

    MONTH_PROFIT("""
            SELECT report.month,
                report.year,
                SUM(product.price * product.quantity) AS amount
                FROM dental_work
                JOIN product ON product.work_id = dental_work.id
                JOIN report ON dental_work.report_id = report.id
                WHERE dental_work.user_id = ? AND year = ? AND month = ?
                GROUP BY dental_work.report_id;
            """);

    public final String QUERY;

    MySqlSamples(String QUERY) {
        this.QUERY = QUERY;
    }

}
