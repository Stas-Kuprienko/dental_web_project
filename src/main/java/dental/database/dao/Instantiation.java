package dental.database.dao;

import java.lang.reflect.Field;

abstract class Instantiation<T> {

    protected abstract void build() throws Exception;

    protected void setObjectPrivateField(T object, String field, Object newValue) throws IllegalAccessException, NoSuchFieldException {
        Field settable = object.getClass().getDeclaredField(field);
        settable.setAccessible(true);
        settable.set(object, newValue);
        settable.setAccessible(false);
    }

}
