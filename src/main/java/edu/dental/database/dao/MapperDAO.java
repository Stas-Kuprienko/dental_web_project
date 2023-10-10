package edu.dental.database.dao;

import edu.dental.database.DatabaseException;
import edu.dental.domain.records.Mapper;

import java.util.Collection;

public interface MapperDAO extends DAO<Mapper.Entry> {

    @Override
    boolean putAll(Collection<Mapper.Entry> list) throws DatabaseException;

    @Override
    boolean put(Mapper.Entry object) throws DatabaseException;

    @Override
    Collection<Mapper.Entry> getAll() throws DatabaseException;

    @Override
    Mapper.Entry get(int id) throws DatabaseException;

    @Override
    Collection<Mapper.Entry> search(Object... args) throws DatabaseException;

    @Override
    boolean edit(Mapper.Entry object) throws DatabaseException;

    @Override
    boolean delete(int id) throws DatabaseException;
}
