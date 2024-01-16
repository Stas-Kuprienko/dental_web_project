package edu.dental.entities;

import edu.dental.database.dao.DAO;

/**
 * Implementation is used in {@linkplain DAO.DatabaseRequest#setID(IDHaving) setID()} method.
 */
public interface IDHaving {

    int getId();

    void setId(int id);

}
