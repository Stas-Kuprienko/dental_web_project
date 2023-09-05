package dental.database.requests;

import dental.database.DBManager;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class PullRequest {

    protected ResultSet result;

    protected void doRequest(String request) throws SQLException {
        Statement state = DBManager.getStatement();
        this.result = state.executeQuery(request);
    }

}
