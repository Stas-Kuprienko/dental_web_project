package dental.database.db_statements.update;

import dental.database.db_statements.PushQuery;

import java.sql.SQLException;


public class RecordUpdateQuery extends PushQuery {

    final String SAMPLE = "UPDATE %s SET %s = '%s' WHERE %s = %s;";

    public RecordUpdateQuery(String table, String column, String newValue, String whereField, String whereValue) throws SQLException {
        String query = String.format(SAMPLE, table, column, newValue, whereField, whereValue);
        doQuery(query);
    }

}