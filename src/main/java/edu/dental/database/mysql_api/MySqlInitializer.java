package edu.dental.database.mysql_api;

import edu.dental.database.TableInitializer;
import edu.dental.utils.DatesTool;

import java.sql.SQLException;
import java.time.LocalDate;

public class MySqlInitializer implements TableInitializer {

    private static final TableInitializer instance;
    private static LocalDate executeAddReports = null;

    private MySqlInitializer() {}
    static {
        instance = new MySqlInitializer();
    }

    public final String DROP = """
                DROP TABLE IF EXISTS product;
                DROP TABLE IF EXISTS dental_work;
                DROP TABLE IF EXISTS product_map;
                DROP TABLE IF EXISTS user;
                """;

    public final String USER_Q = String.format("""
            CREATE TABLE %s (
                id INT NOT NULL AUTO_INCREMENT,
                name VARCHAR(63),
                email VARCHAR(129) NOT NULL UNIQUE,
                password BLOB NOT NULL,
                created DATE NOT NULL,
                PRIMARY KEY (id)
                );""", TableInitializer.USER);

    public final String REPORT_Q = String.format("""
            CREATE TABLE %s (
                id INT NOT NULL AUTO_INCREMENT,
            	year YEAR NOT NULL,
                month ENUM ('january', 'february', 'march',
            					'april', 'may', 'june',
                            'july', 'august', 'september',
                           'october', 'november', 'december') NOT NULL,
                PRIMARY KEY (id)
                );""", TableInitializer.REPORT);

    public final String PRODUCT_MAP_Q = String.format("""
            CREATE TABLE %s (
              user_id INT NOT NULL,
              id INT NOT NULL AUTO_INCREMENT,
              title VARCHAR(30) NOT NULL UNIQUE,
              price INT NOT NULL,
              FOREIGN KEY (user_id) REFERENCES user(id),
              PRIMARY KEY (id, user_id)
              );""", TableInitializer.PRODUCT_MAP);

    public final String DENTAL_WORK_Q = String.format("""
            CREATE TABLE %s (
            	user_id INT NOT NULL,
            	id INT NOT NULL AUTO_INCREMENT,
            	patient VARCHAR(63) NOT NULL,
            	clinic VARCHAR(63) NOT NULL,
            	accepted DATE NOT NULL,
                complete DATE,
                status ENUM('MAKE', 'CLOSED', 'PAID') DEFAULT 'MAKE',
            	photo BLOB,
            	comment VARCHAR(127),
                report_id INT DEFAULT null,
                CONSTRAINT user_id FOREIGN KEY (user_id) REFERENCES user(id),
                FOREIGN KEY (report_id) REFERENCES report (id),
            	PRIMARY KEY (id, user_id)
                );""", TableInitializer.DENTAL_WORK);

    public final String PRODUCT_Q = String.format("""
            CREATE TABLE %s (
                work_id INT NOT NULL,
                title INT NOT NULL,
                quantity SMALLINT DEFAULT 0,
                price INT DEFAULT 0,
                CONSTRAINT work_id FOREIGN KEY (work_id) REFERENCES dental_work(id),
                FOREIGN KEY (title) REFERENCES product_map (id),
                PRIMARY KEY (work_id, title)
                );""", TableInitializer.PRODUCT);

    public final String CREATE_REPORTS = "INSERT INTO " +
                                TableInitializer.REPORT +
                    " (year, month) VALUES (%s, '%s');";


    @Override
    public void init() {
        try {
            @SuppressWarnings("resource")
            Request request = new Request(DROP, USER_Q, REPORT_Q, PRODUCT_MAP_Q, DENTAL_WORK_Q, PRODUCT_Q);
            request.start();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public synchronized void addReports() {
        if (executeAddReports != null &&
                executeAddReports.equals(LocalDate.now())) {
            return;
        }
        int count = 12;
        String[][] yearsNMonths = DatesTool.buildMonthStringArray(count);
        String[] queries = new String[count];
        for (int i = 0; i < count; i++) {
            queries[i] = String.format(CREATE_REPORTS, yearsNMonths[i][0], yearsNMonths[i][1]);
        }
        try {
            @SuppressWarnings("resource")
            Request request = new Request(queries);
            request.start();
            executeAddReports = LocalDate.now();
        } catch (SQLException e) {
            //TODO logger
            throw new RuntimeException(e);
        }
    }

    public static synchronized TableInitializer getInstance() {
        return instance;
    }
}