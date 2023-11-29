package edu.dental.database.dao;

import edu.dental.database.DatabaseException;
import edu.dental.domain.entities.IDentalWork;

import java.util.List;

public interface DentalWorkDAO extends DAO<IDentalWork> {

    @Override
    boolean putAll(List<IDentalWork> list) throws DatabaseException;

    @Override
    boolean put(IDentalWork object) throws DatabaseException;

    @Override
    List<IDentalWork> getAll() throws DatabaseException;

    List<IDentalWork> getAllMonthly(String month, String year) throws DatabaseException;

    @Override
    IDentalWork get(int id) throws DatabaseException;

    @Override
    List<IDentalWork> search(Object... args) throws DatabaseException;

    @Override
    boolean edit(IDentalWork object) throws DatabaseException;

    int setReportId(List<IDentalWork> list) throws  DatabaseException;

    @Override
    boolean delete(int id) throws DatabaseException;
}
