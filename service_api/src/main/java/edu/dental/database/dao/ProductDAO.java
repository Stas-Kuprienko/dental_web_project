package edu.dental.database.dao;

import edu.dental.database.DatabaseException;
import edu.dental.domain.entities.Product;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public interface ProductDAO {

    boolean putAll(List<Product> list) throws DatabaseException;

    boolean put(Product product) throws DatabaseException;

    List<Product> instantiate(ResultSet resultSet) throws SQLException, DatabaseException;

    List<Product> getAll() throws DatabaseException;

    List<Product> search(String title, int quantity) throws DatabaseException;

    boolean overwrite(List<Product> list) throws DatabaseException;

    boolean deleteAll() throws DatabaseException;

    boolean delete(String title) throws DatabaseException;
}
