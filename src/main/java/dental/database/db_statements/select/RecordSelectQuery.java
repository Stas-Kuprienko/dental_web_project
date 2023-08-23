package dental.database.db_statements.select;

import dental.database.db_statements.SelectQuery;

import java.sql.SQLException;

public class RecordSelectQuery extends SelectQuery {

    final String SAMPLE1 = "SELECT %s FROM %s";

    final String SAMPLE2 = " WHERE %s = '%s';";

    public RecordSelectQuery(String selectable, String from, String whereField, String whereValue) throws SQLException {
        String query = SAMPLE1 + SAMPLE2;
        query = String.format(query, selectable, from, whereField, whereValue);

        selectData(query);
    }

    public RecordSelectQuery(String selectable, String from) throws SQLException {
        String query = SAMPLE1 + ";";
        query = String.format(query, selectable, from);

        selectData(query);
    }
}