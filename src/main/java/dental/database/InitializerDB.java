package dental.database;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public final class InitializerDB extends Thread {

    private static final InitializerDB instance;
    private InitializerDB() {}

    static {
        instance = new InitializerDB();
    }

    public final String DROP = "DROP TABLE IF EXISTS mydb.";

    public final String ACCOUNT = """
            CREATE TABLE mydb.account (
                id INT NOT NULL,
                login VARCHAR(15) NOT NULL,
                PRIMARY KEY (id)
                );""";

    public final String WORK_RECORD = """
            CREATE TABLE mydb.work_record (
            	account_id INT NOT NULL,
            	FOREIGN KEY(account_id) REFERENCES mydb.account(id) ON DELETE CASCADE,
            	id INT NOT NULL,
            	patient VARCHAR(20),
            	clinic VARCHAR(20),
            	complete DATE,
            	accepted DATE NOT NULL,
            	closed BOOLEAN,
            	PRIMARY KEY (id)
                );""";

    public final String PRODUCT = """
            CREATE TABLE mydb.product (
            	work_id INT NOT NULL,
                title VARCHAR(15) NOT NULL,
                quantity SMALLINT,
                price INT NOT NULL,
                FOREIGN KEY(work_id) REFERENCES mydb.work_record(id) ON DELETE CASCADE
                );""";

    public final String REPORT = """
            CREATE TABLE mydb.report (
            	r_year YEAR NOT NULL,
            	r_month ENUM('january', 'february', 'march',
            					'april', 'may', 'june', 'july',
                                'august', 'september', 'october',
                                'november', 'december') NOT NULL,
            	r_id INT NOT NULL AUTO_INCREMENT,
            	account_id INT NOT NULL,
                FOREIGN KEY(account_id) REFERENCES mydb.account(id)  ON DELETE CASCADE,
            	PRIMARY KEY (r_id, account_id)
            	);""";


    @Override
    public void run() {
        try {
            Connection connection = ConnectionPool.get();
            Statement statement = connection.createStatement();
            statement.addBatch(DROP + "report");
            statement.addBatch(DROP + "product");
            statement.addBatch(DROP + "work_record");
            statement.addBatch(DROP + "account");
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
