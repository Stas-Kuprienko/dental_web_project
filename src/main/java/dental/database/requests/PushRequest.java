package dental.database.requests;

import dental.database.ConnectionPool;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * The class for extending SQL query classes.
 */
public class PushRequest {

    /**
     * Execute the given SQL requests to a DB.
     * @param request The request String.
     * @throws SQLException If a database access error occurs or
     * this method is called on a closed {@code Statement}.
     */
    protected void doRequest(String request) throws SQLException {
        Connection connection = ConnectionPool.get();
        connection.createStatement().execute(request);
        ConnectionPool.put(connection);
    }

}
