package edu.dental.database.dao;

import edu.dental.database.DatabaseException;
import edu.dental.domain.entities.Product;

import java.util.Collection;

public interface ProductDAO extends DAO<Product> {

    @Override
    boolean putAll(Collection<Product> list) throws DatabaseException;

    @Override
    boolean put(Product object) throws DatabaseException;

    @Override
    Collection<Product> getAll() throws DatabaseException;

    @Override
    Product get(int id) throws DatabaseException;

    @Override
    Collection<Product> search(Object... args) throws DatabaseException;

    @Override
    boolean edit(Product object) throws DatabaseException;

    @Override
    boolean delete(int id) throws DatabaseException;
}
