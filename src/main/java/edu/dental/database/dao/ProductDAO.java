package edu.dental.database.dao;

import edu.dental.database.DatabaseException;
import edu.dental.domain.entities.Product;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;

public interface ProductDAO {

    boolean putAll(Collection<Product> list) throws DatabaseException;

    boolean put(Product product) throws DatabaseException;

    Collection<Product> instantiate(ResultSet resultSet) throws SQLException, DatabaseException;

    Collection<Product> getAll() throws DatabaseException;

    Collection<Product> search(String title, int quantity) throws DatabaseException;

    boolean overwrite(Collection<Product> list) throws DatabaseException;

    boolean deleteAll() throws DatabaseException;

    boolean delete(String title) throws DatabaseException;
}
