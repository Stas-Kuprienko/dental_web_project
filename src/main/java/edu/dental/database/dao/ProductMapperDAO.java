package edu.dental.database.dao;

import edu.dental.database.DatabaseException;
import edu.dental.domain.records.ProductMapper;

import java.util.Collection;

public interface ProductMapperDAO extends DAO<ProductMapper .Entry> {

    @Override
    boolean putAll(Collection<ProductMapper.Entry> list) throws DatabaseException;

    @Override
    boolean put(ProductMapper.Entry object) throws DatabaseException;

    @Override
    Collection<ProductMapper.Entry> getAll() throws DatabaseException;

    @Override
    ProductMapper.Entry get(int id) throws DatabaseException;

    @Override
    Collection<ProductMapper.Entry> search(Object... args) throws DatabaseException;

    @Override
    boolean edit(ProductMapper.Entry object) throws DatabaseException;

    @Override
    boolean delete(int id) throws DatabaseException;
}
