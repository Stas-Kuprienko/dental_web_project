package edu.dental.database.mysql_api;

import edu.dental.database.TableInitializer;

import java.sql.SQLException;

public class MySqlInitializer implements TableInitializer {

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
              title VARCHAR(30) NOT NULL,
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


    @Override
    public void init() {
        try (Request request = new Request(DROP, USER_Q, REPORT_Q, PRODUCT_MAP_Q, DENTAL_WORK_Q, PRODUCT_Q)) {
            request.start();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}