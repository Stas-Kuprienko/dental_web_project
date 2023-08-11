package dental.database.db_statements;

import dental.database.DBManager;

import java.sql.SQLException;
import java.sql.Statement;

interface IQuery {

    Statement state = DBManager.getStatement();

    default boolean doQuery(String query) {
        try {
            return IQuery.state != null && IQuery.state.execute(query);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
