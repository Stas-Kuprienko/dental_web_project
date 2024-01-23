package edu.dental.database.dao;

import edu.dental.database.DatabaseException;
import edu.dental.entities.DentalWork;

import java.util.List;

public interface DentalWorkDAO {

    boolean putAll(List<DentalWork> list) throws DatabaseException;

    boolean put(DentalWork object) throws DatabaseException;

    List<DentalWork> getAll(int userId) throws DatabaseException;

    List<DentalWork> getAllMonthly(int userId, String month, int year) throws DatabaseException;

    DentalWork get(int userId, int id) throws DatabaseException;

    List<DentalWork> search(int userId, String[] fields, String[] args) throws DatabaseException;

    boolean update(DentalWork object) throws DatabaseException;

    boolean setFieldValue(int userId, List<DentalWork> list, String field, Object value) throws DatabaseException;

    int setReportId(int userId, List<DentalWork> list, String month, String year) throws  DatabaseException;

    int getReportId(String month, String year) throws DatabaseException;

    boolean delete(int id) throws DatabaseException;
}
