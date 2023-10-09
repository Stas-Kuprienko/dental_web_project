package edu.dental.database.dao;

import edu.dental.database.DatabaseException;
import edu.dental.domain.entities.WorkRecord;

import java.util.Collection;

public interface WorkRecordDAO extends DAO<WorkRecord> {

    @Override
    boolean putAll(Collection<WorkRecord> list) throws DatabaseException;

    @Override
    boolean put(WorkRecord object) throws DatabaseException;

    @Override
    Collection<WorkRecord> getAll() throws DatabaseException;

    @Override
    WorkRecord get(int id) throws DatabaseException;

    @Override
    Collection<WorkRecord> search(Object... args) throws DatabaseException;

    @Override
    boolean edit(WorkRecord object) throws DatabaseException;

    @Override
    boolean delete(int id) throws DatabaseException;
}
