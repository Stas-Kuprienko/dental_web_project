package dental.database.db_statements;

import dental.database.DBManager;

import java.sql.SQLException;
import java.sql.Statement;

public interface IQuery {

    Statement state = DBManager.getStatement();

    default void doQuery(String query) {
        try {
            if (IQuery.state != null) {
                IQuery.state.execute(query);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
