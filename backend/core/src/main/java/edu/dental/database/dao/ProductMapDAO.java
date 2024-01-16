package edu.dental.database.dao;

import edu.dental.database.DatabaseException;
import edu.dental.entities.ProductMap;

public interface ProductMapDAO {

    boolean putAll(ProductMap map) throws DatabaseException;

    int put(String key, int value) throws DatabaseException;

    ProductMap get() throws DatabaseException;

    boolean edit(int id, int value) throws DatabaseException;

    boolean delete(int id) throws DatabaseException;
}
