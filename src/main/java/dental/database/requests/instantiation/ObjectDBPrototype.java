package dental.database.requests.instantiation;

import dental.domain.MyList;
import dental.database.requests.PullRequest;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;

/**
 * The abstract class to create objects by database values.
 * @param <T> The type of creatable object.
 */
public abstract class ObjectDBPrototype<T> extends PullRequest {

    protected final MyList<T> list;

    final String SAMPLE1 = "SELECT %s FROM %s";

    final String SAMPLE2 = " WHERE %s = '%s';";

    public ObjectDBPrototype(String selectable, String from, String whereField, String whereValue) throws SQLException {
        String request = SAMPLE1 + SAMPLE2;
        request = String.format(request, selectable, from, whereField, whereValue);

        doRequest(request);
        this.list = new MyList<>();
    }

    public ObjectDBPrototype(String selectable, String from) throws SQLException {
        String request = SAMPLE1 + ";";
        request = String.format(request, selectable, from);

        doRequest(request);
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