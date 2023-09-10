package dental.database;

import dental.database.dao.AccountDAO;
import dental.database.dao.ProductDAO;
import dental.database.dao.ReportDAO;
import dental.database.dao.WorkRecordDAO;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public final class InitializerDB extends Thread {

    private static final InitializerDB instance;
    private InitializerDB() {}

    static {
        instance = new InitializerDB();
    }

    public final String DROP = String.format("DROP TABLE IF EXISTS %s.", DBConfig.DATA_BASE);

    public final String ACCOUNT = String.format("""
            CREATE TABLE %s.%s (
                id INT NOT NULL AUTO_INCREMENT,
                name VARCHAR(25),
                login VARCHAR(15) NOT NULL,
                password BLOB NOT NULL,
                created DATE NOT NULL,
                PRIMARY KEY (id)
            );""", DBConfig.DATA_BASE, AccountDAO.TABLE_NAME);

    public final String WORK_RECORD = String.format("""
            CREATE TABLE %s.%s (
                id INT NOT NULL AUTO_INCREMENT,
            	account_id INT NOT NULL,
            	patient VARCHAR(20),
                clinic VARCHAR(20),
                complete DATE,
            	accepted DATE NOT NULL,
                closed BOOLEAN,
                photo BLOB,
                comment VARCHAR(45),
                PRIMARY KEY (id),
                FOREIGN KEY(account_id) REFERENCES %s(id) ON DELETE CASCADE
            );""", DBConfig.DATA_BASE, WorkRecordDAO.TABLE_NAME, AccountDAO.TABLE_NAME);

    public final String PRODUCT = String.format("""
            CREATE TABLE %s.%s (
                work_id INT NOT NULL,
                title VARCHAR(15) NOT NULL,
                quantity SMALLINT,
                price INT NOT NULL,
                FOREIGN KEY(work_id) REFERENCES %s(id) ON DELETE CASCADE
            );""",DBConfig.DATA_BASE, ProductDAO.TABLE_NAME, WorkRecordDAO.TABLE_NAME);

    public final String REPORT = String.format("""
            CREATE TABLE %s.%s (
                r_year YEAR NOT NULL,
                r_month ENUM('january', 'february', 'march',
            				'april', 'may', 'june', 'july',
                            'august', 'september', 'october',
                            'november', 'december') NOT NULL,
                r_id INT NOT NULL AUTO_INCREMENT,
                account_id INT NOT NULL,
                FOREIGN KEY(account_id) REFERENCES %s(id) ON DELETE CASCADE,
                PRIMARY KEY (r_id, account_id)
            );""", DBConfig.DATA_BASE, ReportDAO.TABLE_NAME, AccountDAO.TABLE_NAME);


    @Override
    public void run() {
        try {
            Connection connection = ConnectionPool.get();
            Statement statement = connection.createStatement();
            statement.addBatch(DROP + ReportDAO.TABLE_NAME);
            statement.addBatch(DROP + ProductDAO.TABLE_NAME);
            statement.addBatch(DROP + WorkRecordDAO.TABLE_NAME);
            statement.addBatch(DROP + AccountDAO.TABLE_NAME);
            statement.addBatch(ACCOUNT);
            statement.addBatch(WORK_RECORD);
            statement.addBatch(PRODUCT);
            statement.addBatch(REPORT);
            statement.executeBatch();
            ConnectionPool.put(connection);
        } catch (SQLException e) {
            //TODO loggers
            throw new RuntimeException(e);
        }
    }

    public static void initializeDataBase() {
        instance.start();
    }
}
