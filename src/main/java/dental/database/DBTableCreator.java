package dental.database;

import dental.app.userset.Account;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;

public final class DBTableCreator {

    private DBTableCreator() {}
    static {
        instance = new DBTableCreator();
    }
    private static final DBTableCreator instance;

    //TODO

    private static final String QUERY = """
            CREATE TABLE %s (
                id INT NOT NULL AUTO_INCREMENT,
            	patient VARCHAR(20),
                clinic VARCHAR(20),
                %s
                complete DATE,
            	accepted DATE NOT NULL,
                closed BOOLEAN NOT NULL DEFAULT 0,
                paid BOOLEAN NOT NULL DEFAULT 0,
                photo BLOB,
                comment VARCHAR(45),
                PRIMARY KEY (id)
            );""";


    public boolean createWorkRecords(Account account) {
        HashMap<String, Integer> productMap =
                account.recordManager != null ? account.recordManager.getProductMap() : null;
        if ((productMap == null)||(productMap.isEmpty())) {
            throw new NullPointerException();
        }
        String productColumns = concatProductColumns(productMap);
        String tableName = "work_record_" + account.getId();
        String finalQuery = String.format(QUERY, tableName, productColumns);
        try {
            return request(finalQuery);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private String concatProductColumns(HashMap<String, Integer> productMap) {
        StringBuilder result = new StringBuilder();
        for (String s : productMap.keySet()) {
            result.append(String.format("\t%s INT DEFAULT 0,\n", s));
        }
        return result.toString();
    }

    private boolean request(String query) throws SQLException {
        Connection connection = ConnectionPool.get();
        Statement statement = connection.createStatement();
        boolean isSuccess = statement.execute(query);
        statement.close();
        ConnectionPool.put(connection);
        return isSuccess;
    }

    public static synchronized DBTableCreator getInstance() {
        return instance;
    }
}
