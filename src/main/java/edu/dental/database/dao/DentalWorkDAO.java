package edu.dental.database.dao;

import edu.dental.database.DatabaseException;
import edu.dental.domain.entities.I_DentalWork;

import java.util.List;

public interface DentalWorkDAO extends DAO<I_DentalWork> {

    @Override
    boolean putAll(List<I_DentalWork> list) throws DatabaseException;

    @Override
    boolean put(I_DentalWork object) throws DatabaseException;

    @Override
    List<I_DentalWork> getAll() throws DatabaseException;

    List<I_DentalWork> getAllMonthly(String month, String year) throws DatabaseException;

    @Override
    I_DentalWork get(int id) throws DatabaseException;

    @Override
    List<I_DentalWork> search(Object... args) throws DatabaseException;

    @Override
    boolean edit(I_DentalWork object) throws DatabaseException;

    int setReportId(List<I_DentalWork> list) throws  DatabaseException;

    @Override
    boolean delete(int id) throws DatabaseException;
}
