package edu.dental.database.dao;

import edu.dental.database.DatabaseException;
import edu.dental.entities.DentalWork;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public interface DentalWorkDAO {

    boolean putAll(List<DentalWork> list) throws DatabaseException;

    boolean put(DentalWork object) throws DatabaseException;

    List<DentalWork> getAll(int userId) throws DatabaseException;

    List<DentalWork> getAllMonthly(int userId, String month, String year) throws DatabaseException;

    DentalWork get(int id) throws DatabaseException;

    List<DentalWork> search(int userId, String[] fields, String[] args) throws DatabaseException;

    boolean edit(DentalWork object) throws DatabaseException;

    boolean setFieldValue(int userId, List<DentalWork> list, String field, Object value) throws DatabaseException;

    int setReportId(List<DentalWork> list, String month, String year) throws  DatabaseException;

    boolean delete(int id) throws DatabaseException;

    class Request extends DAORequest {
        public Request(String query) throws SQLException {
            super(query);
        }

        public Request() throws SQLException {
            super();
        }
    }

    interface Instantiating<T> {

        List<T> build() throws SQLException, IOException, DatabaseException;

    }
}
