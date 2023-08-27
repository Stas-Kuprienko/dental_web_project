package dental.database.db_statements.select;

import dental.app.MyList;
import dental.database.db_statements.SelectQuery;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;

/**
 * The abstract class to create objects by database values.
 * @param <T> The type of creatable object.
 */
public abstract class ObjectDBPrototype<T> extends SelectQuery {

    protected final MyList<T> list;

    final String SAMPLE1 = "SELECT %s FROM %s";

    final String SAMPLE2 = " WHERE %s = '%s';";

    public ObjectDBPrototype(String selectable, String from, String whereField, String whereValue) throws SQLException {
        String query = SAMPLE1 + SAMPLE2;
        query = String.format(query, selectable, from, whereField, whereValue);

        doQuery(query);
        this.list = new MyList<>();
    }

    public ObjectDBPrototype(String selectable, String from) throws SQLException {
        String query = SAMPLE1 + ";";
        query = String.format(query, selectable, from);

        doQuery(query);
        this.list = new MyList<>();
    }

    public abstract ObjectDBPrototype<T> build() throws
            SQLException, IllegalAccessException, NoSuchMethodException, InvocationTargetException, InstantiationException;

    protected void setObjectPrivateField(T object, Field[] fields, String field, Object newValue) throws IllegalAccessException {
        Field settable;
        for (Field f : fields) {
            if (f.toString().endsWith(field)) {
                settable = f;
                settable.setAccessible(true);
                settable.set(object, newValue);
                settable.setAccessible(false);
                break;
            }
        }
    }

    public MyList<T> getList() {
        return list;
    }
}