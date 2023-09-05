package dental.database.requests;

import dental.database.DBManager;

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
        Statement state = DBManager.getStatement();
        state.execute(request);
    }

}
