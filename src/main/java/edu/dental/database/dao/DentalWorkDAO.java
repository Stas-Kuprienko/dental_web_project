package edu.dental.database.dao;

import edu.dental.database.DatabaseException;
import edu.dental.domain.entities.I_DentalWork;

import java.util.Collection;

public interface DentalWorkDAO extends DAO<I_DentalWork> {

    @Override
    boolean putAll(Collection<I_DentalWork> list) throws DatabaseException;

    @Override
    boolean put(I_DentalWork object) throws DatabaseException;

    @Override
    Collection<I_DentalWork> getAll() throws DatabaseException;

    Collection<I_DentalWork> getAllMonthly(String month, String year) throws DatabaseException;

    @Override
    I_DentalWork get(int id) throws DatabaseException;

    @Override
    Collection<I_DentalWork> search(Object... args) throws DatabaseException;

    @Override
    boolean edit(I_DentalWork object) throws DatabaseException;

    boolean setFieldValue(Collection<I_DentalWork> list, String field, Object value) throws  DatabaseException;

    @Override
    boolean delete(int id) throws DatabaseException;
}
