package dental.database.db_statements;

import dental.database.DBManager;

import java.sql.SQLException;
import java.sql.Statement;

/**
 * The class for extending SQL query classes.
 */
public class IQuery {

    private final Statement state = DBManager.getStatement();

    /**
     * Execute the given SQL statement to a DB.
     * @param query The required query string.
     * @throws SQLException If a database access error occurs or
     * this method is called on a closed {@code Statement}.
     */
    protected void doQuery(String query) throws SQLException {

        if (state != null) {

            state.execute(query);
        }
        else throw new SQLException("Statement is null");
    }

}
