package dental.database.requests.update;

import dental.database.requests.PushRequest;

import java.sql.SQLException;


public class RecordUpdateRequest extends PushRequest {

    final String SAMPLE = "UPDATE %s SET %s = '%s' WHERE %s = %s;";

    public RecordUpdateRequest(String table, String column, String newValue, String whereField, String whereValue) throws SQLException {
        String request = String.format(SAMPLE, table, column, newValue, whereField, whereValue);
        doRequest(request);
    }

}