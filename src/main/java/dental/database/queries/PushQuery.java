package dental.database.queries;

import dental.database.DBManager;

import java.sql.SQLException;
import java.sql.Statement;

/**
 * The class for extending SQL query classes.
 */
public class PushQuery {

    /**
     * Execute the given SQL statement to a DB.
     * @param query The query String.
     * @throws SQLException If a database access error occurs or
     * this method is called on a closed {@code Statement}.
     */
    protected void doQuery(String query) throws SQLException {
        Statement state = DBManager.getStatement();
        state.execute(query);
    }

}
