package dental.database.requests.instantiation;

import dental.app.MyList;

public interface DAO<E> {

    void insert(E e);

    MyList<E> getAll();

    E get(int id);

    void remove(int id);
    void remove(E e);

}
