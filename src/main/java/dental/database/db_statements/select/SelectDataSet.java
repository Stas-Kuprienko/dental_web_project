package dental.database.db_statements.select;

import dental.database.db_statements.SelectQuery;

import java.sql.SQLException;

public abstract class SelectDataSet<T> extends RecordSelectQuery {

    private final T object;

    public SelectDataSet(String selectable, String from, String whereField, String whereValue, T object) throws SQLException {
        super(selectable, from, whereField, whereValue);
        this.object = object;
    }

    public SelectDataSet(String selectable, String from, T object) throws SQLException {
        super(selectable, from);
        this.object = object;
    }

    public T getObject() {
        return object;
    }
}
