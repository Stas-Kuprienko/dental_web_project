package edu.dental.database.dao;

import edu.dental.database.DatabaseException;
import edu.dental.domain.entities.DentalWork;

import java.util.List;

public interface DentalWorkDAO extends DAO<DentalWork> {

    @Override
    boolean putAll(List<DentalWork> list) throws DatabaseException;

    @Override
    boolean put(DentalWork object) throws DatabaseException;

    @Override
    List<DentalWork> getAll() throws DatabaseException;

    List<DentalWork> getAllMonthly(String month, String year) throws DatabaseException;

    @Override
    DentalWork get(int id) throws DatabaseException;

    @Override
    List<DentalWork> search(Object... args) throws DatabaseException;

    @Override
    boolean edit(DentalWork object) throws DatabaseException;

    boolean setFieldValue(List<DentalWork> list, String field, Object value) throws DatabaseException;

    int setReportId(List<DentalWork> list) throws  DatabaseException;

    @Override
    boolean delete(int id) throws DatabaseException;
}
