package edu.dental.entities;

/**
 * Implementation is used in {@linkplain edu.dental.database.dao.IRequest#setID(IDHaving) setID()} method.
 */
public interface IDHaving {

    int getId();

    void setId(int id);

}
