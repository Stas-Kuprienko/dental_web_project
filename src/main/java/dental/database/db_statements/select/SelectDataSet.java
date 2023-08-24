package dental.database.db_statements.select;

import dental.database.db_statements.SelectQuery;

import java.sql.SQLException;

/**
 * The abstract class to create objects by database values.
 * @param <T> The type of creatable object.
 */
public abstract class SelectDataSet<T> extends SelectQuery {

    private final T object;

    final String SAMPLE1 = "SELECT %s FROM %s";

    final String SAMPLE2 = " WHERE %s = '%s';";

    public SelectDataSet(String selectable, String from, String whereField, String whereValue) throws SQLException {
        String query = SAMPLE1 + SAMPLE2;
        query = String.format(query, selectable, from, whereField, whereValue);

        doQuery(query);
        this.object = build();
    }

    public SelectDataSet(String selectable, String from) throws SQLException {
        String query = SAMPLE1 + ";";
        query = String.format(query, selectable, from);

        doQuery(query);
        this.object = build();
    }

    protected abstract T build();

    public T getObject() {
        return object;
    }
}