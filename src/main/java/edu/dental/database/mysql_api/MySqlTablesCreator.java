package edu.dental.database.mysql_api;

import edu.dental.database.DatabaseException;
import edu.dental.database.connection.DBConfiguration;
import edu.dental.database.TablesCreator;

import java.sql.SQLException;

public class MySqlTablesCreator implements TablesCreator {

    private MySqlTablesCreator() {}
    static {
        instance = new MySqlTablesCreator();
    }

    static final MySqlTablesCreator instance;

    public final String PRODUCT_MAP = """
            CREATE TABLE %s (
              id INT NOT NULL AUTO_INCREMENT,
              title VARCHAR(15) NOT NULL,
              price INT NOT NULL,
              PRIMARY KEY (id)
              );""";

    public final String WORK_RECORD = """
            CREATE TABLE %s (
            	id INT NOT NULL AUTO_INCREMENT,
            	patient VARCHAR(20) NOT NULL,
            	clinic VARCHAR(20) NOT NULL,
            	accepted DATE NOT NULL,
                complete DATE,
                closed BOOLEAN NOT NULL DEFAULT 0,
                paid BOOLEAN NOT NULL DEFAULT 0,
            	photo BLOB,
            	comment VARCHAR(45),
            	PRIMARY KEY (id)
                );""";

    public final String PRODUCT = """
            CREATE TABLE %s (
                work_id INT NOT NULL,
                title VARCHAR(15) NOT NULL,
                quantity SMALLINT DEFAULT 0,
                price INT DEFAULT 0,
                CONSTRAINT work_id FOREIGN KEY (work_id) REFERENCES work_record_%s(id)
                );""";

    @Override
    public void createProductMapTable(int userID) throws DatabaseException {
        String table = DBConfiguration.DATA_BASE + ".product_map_" + userID;
        String query = String.format(PRODUCT_MAP, table);
        try (Request request = new Request(query)) {
            request.execute();
        } catch (SQLException e) {
            throw new DatabaseException(e.getMessage(), e.getCause());
        }
    }

    @Override
    public void createWorkRecordTable(int userID) throws DatabaseException {
        String table = DBConfiguration.DATA_BASE + ".work_record_" + userID;
        String query = String.format(WORK_RECORD, table);
        try (Request request = new Request(query)) {
            if (request.execute()) {
                createProductTable(userID);
            }
        } catch (SQLException e) {
            throw new DatabaseException(e.getMessage(), e.getCause());
        }
    }

    public void createWorkRecordTable(int userID, String yearMonth) throws DatabaseException {
        String table = DBConfiguration.DATA_BASE + ".work_record_" + userID + "_" + yearMonth;
        String query = String.format(WORK_RECORD, table);
        try (Request request = new Request(query)) {
            if (request.execute()) {
                createProductTable(userID, yearMonth);
            }
        } catch (SQLException e) {
            throw new DatabaseException(e.getMessage(), e.getCause());
        }
    }

    @Override
    public void createProductTable(int userID) throws DatabaseException {
        String table = DBConfiguration.DATA_BASE + ".product_" + userID;
        String query = String.format(PRODUCT, table, userID);
        try (Request request = new Request(query)) {
            request.execute();
        } catch (SQLException e) {
            throw new DatabaseException(e.getMessage(), e.getCause());
        }
    }

    public void createProductTable(int userID, String yearMonth) throws DatabaseException {
        String table = DBConfiguration.DATA_BASE + ".product_" + userID + "_" + yearMonth;
        String query = String.format(PRODUCT, table, userID + "_" + yearMonth);
        try (Request request = new Request(query)) {
            request.execute();
        } catch (SQLException e) {
            throw new DatabaseException(e.getMessage(), e.getCause());
        }
    }
}