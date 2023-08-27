package dental.database.db_statements;

import dental.database.DBManager;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class SelectQuery {

    protected ResultSet result;

    protected void doQuery(String query) throws SQLException {
        Statement state = DBManager.getStatement();
        this.result = state.executeQuery(query);
    }

}
