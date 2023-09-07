package dental.database.dao;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;

abstract class Instantiation<T> {

    protected abstract void build() throws SQLException, IOException, NoSuchFieldException, IllegalAccessException;

    protected void setObjectPrivateField(T object, String field, Object newValue) throws IllegalAccessException, NoSuchFieldException {
        Field settable = object.getClass().getDeclaredField(field);
        settable.setAccessible(true);
        settable.set(object, newValue);
        settable.setAccessible(false);
    }

}
