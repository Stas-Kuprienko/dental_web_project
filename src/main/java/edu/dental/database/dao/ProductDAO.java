package edu.dental.database.dao;

import edu.dental.database.DatabaseException;
import edu.dental.domain.entities.Product;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;

public interface ProductDAO {

    boolean putAll(Collection<Product> list) throws DatabaseException;

    boolean put(Product object) throws DatabaseException;

    Collection<Product> instantiate(ResultSet resultSet) throws SQLException;

    Collection<Product> getAll() throws DatabaseException;

    Collection<Product> search(Object... args) throws DatabaseException;

    boolean edit(Product object) throws DatabaseException;

    boolean delete(int id) throws DatabaseException;
}
