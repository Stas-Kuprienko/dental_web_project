package edu.dental.database.dao;

import edu.dental.database.DatabaseException;
import edu.dental.domain.entities.I_WorkRecord;

import java.util.Collection;

public interface WorkRecordDAO extends DAO<I_WorkRecord> {

    @Override
    boolean putAll(Collection<I_WorkRecord> list) throws DatabaseException;

    @Override
    boolean put(I_WorkRecord object) throws DatabaseException;

    @Override
    Collection<I_WorkRecord> getAll() throws DatabaseException;

    Collection<I_WorkRecord> getAllMonthly(String month, String year) throws DatabaseException;

    @Override
    I_WorkRecord get(int id) throws DatabaseException;

    @Override
    Collection<I_WorkRecord> search(Object... args) throws DatabaseException;

    @Override
    boolean edit(I_WorkRecord object) throws DatabaseException;

    @Override
    boolean delete(int id) throws DatabaseException;
}
