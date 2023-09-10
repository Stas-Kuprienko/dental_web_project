package dental.database.requests;

import dental.database.ConnectionPool;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PullRequest {

    protected ResultSet resultSet;

    protected void doRequest(String request) throws SQLException {
        Connection connection = ConnectionPool.get();
        this.resultSet = connection.createStatement().executeQuery(request);
        ConnectionPool.put(connection);
    }

}
