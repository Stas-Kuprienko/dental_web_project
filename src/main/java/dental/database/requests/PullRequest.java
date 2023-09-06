package dental.database.requests;

import dental.database.ConnectionPool;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class PullRequest {

    protected ResultSet result;

    protected void doRequest(String request) throws SQLException {
        Connection connection = ConnectionPool.get();
        this.result = connection.createStatement().executeQuery(request);
        ConnectionPool.put(connection);
    }

}
