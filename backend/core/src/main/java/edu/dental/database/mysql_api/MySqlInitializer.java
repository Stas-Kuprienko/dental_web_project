package edu.dental.database.mysql_api;

import edu.dental.database.TableInitializer;
import edu.dental.database.connection.DBConfiguration;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.Month;

public class MySqlInitializer implements TableInitializer {

    private static LocalDate executeAddReports = null;

    MySqlInitializer() {}


    public static final String USER_Q = String.format("""
            CREATE TABLE IF NOT EXISTS %s (
            	id INT NOT NULL AUTO_INCREMENT,
            	name VARCHAR(63),
            	email VARCHAR(129) NOT NULL UNIQUE,
            	password BLOB NOT NULL,
            	created DATE NOT NULL,
            	PRIMARY KEY (id, email)
                );""", TableInitializer.USER);

    public static final String REPORT_Q = String.format("""
            CREATE TABLE IF NOT EXISTS %s (
            	id INT NOT NULL AUTO_INCREMENT,
            	year INT NOT NULL,
                month ENUM ('january', 'february', 'march',
            					'april', 'may', 'june',
                            'july', 'august', 'september',
                           'october', 'november', 'december') NOT NULL,
                PRIMARY KEY (id, year, month)
                );""", TableInitializer.REPORT);

    public static final String PRODUCT_MAP_Q = String.format("""
            CREATE TABLE IF NOT EXISTS %s (
            	user_id INT NOT NULL,
            	id INT NOT NULL AUTO_INCREMENT,
            	title VARCHAR(31) NOT NULL UNIQUE,
            	price INT NOT NULL,
            	CONSTRAINT FOREIGN KEY (user_id) REFERENCES user(id) ON DELETE CASCADE,
            	PRIMARY KEY (id, user_id, title)
                );""", TableInitializer.PRODUCT_MAP);

    public static final String DENTAL_WORK_Q = String.format("""
            CREATE TABLE IF NOT EXISTS %s (
            	user_id INT NOT NULL,
            	id INT NOT NULL AUTO_INCREMENT,
            	patient VARCHAR(63) NOT NULL,
            	clinic VARCHAR(63) NOT NULL,
            	accepted DATE NOT NULL,
                complete DATE,
                status ENUM('MAKE', 'CLOSED', 'PAID') DEFAULT 'MAKE',
            	comment VARCHAR(127),
                report_id INT DEFAULT null,
                FOREIGN KEY (report_id) REFERENCES report (id),
                CONSTRAINT user_id FOREIGN KEY (user_id) REFERENCES user(id) ON DELETE CASCADE,
            	PRIMARY KEY (id, user_id)
                );""", TableInitializer.DENTAL_WORK);

    public static final String PRODUCT_Q = String.format("""
            CREATE TABLE IF NOT EXISTS %s (
                work_id INT NOT NULL,
                title INT NOT NULL,
                quantity SMALLINT DEFAULT 0,
                price INT DEFAULT 0,
                CONSTRAINT work_id FOREIGN KEY (work_id) REFERENCES dental_work(id) ON DELETE CASCADE,
                CONSTRAINT FOREIGN KEY (title) REFERENCES product_map (id) ON DELETE CASCADE,
                PRIMARY KEY (work_id, title)
                );""", TableInitializer.PRODUCT);

    public static final String PHOTO_Q = String.format("""
            CREATE TABLE IF NOT EXISTS %s (
            	id INT NOT NULL AUTO_INCREMENT,
                work_id INT NOT NULL,
                file BLOB NOT NULL,
                CONSTRAINT FOREIGN KEY (work_id) REFERENCES dental_work(id),
                PRIMARY KEY (id, work_id)
                );""", TableInitializer.PHOTO);

    public static final String CREATE_REPORTS = "INSERT INTO " +
                                TableInitializer.REPORT +
                    " (year, month) VALUES (%s, '%s');";


    @Override
    public void init() {
        try {
            @SuppressWarnings("resource")
            Request request = new Request(
                    USER_Q, REPORT_Q, PRODUCT_MAP_Q, DENTAL_WORK_Q, PRODUCT_Q, PHOTO_Q);
            request.start();
        } catch (SQLException e) {
            DBConfiguration.logging(e);
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
        String[][] yearsNMonths = buildMonthStringArray(count);
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
            DBConfiguration.logging(e);
            throw new RuntimeException(e);
        }
    }


    private String[][] buildMonthStringArray(int count) {
        int year = LocalDate.now().getYear();
        Month month = LocalDate.now().getMonth().minus(1);
        String[][] result = new String[count][2];
        for (int i = 0; i < count; i++) {
            String[] yearNMonth = new String[] {String.valueOf(year), month.toString()};
            result[i] = yearNMonth;
            month = month.plus(1);
            if (month.equals(Month.JANUARY)) {
                year += 1;
            }
        }
        return result;
    }
}