package edu.dental.database.dao;

import edu.dental.database.DatabaseException;
import edu.dental.domain.records.ProductMap;

import java.util.Collection;

public interface ProductMapDAO extends DAO<ProductMap.Item> {

    @Override
    boolean putAll(Collection<ProductMap.Item> list) throws DatabaseException;

    @Override
    boolean put(ProductMap.Item object) throws DatabaseException;

    @Override
    Collection<ProductMap.Item> getAll() throws DatabaseException;

    @Override
    ProductMap.Item get(int id) throws DatabaseException;

    @Override
    Collection<ProductMap.Item> search(Object... args) throws DatabaseException;

    @Override
    boolean edit(ProductMap.Item object) throws DatabaseException;

    @Override
    boolean delete(int id) throws DatabaseException;
}
