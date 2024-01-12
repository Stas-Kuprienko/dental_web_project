package edu.dental.database.dao;

import edu.dental.database.DatabaseException;
import edu.dental.entities.ProfitRecord;

import java.sql.SQLException;

public interface ProfitRecordDAO {

    ProfitRecord[] countAllProfits(int userId) throws DatabaseException;

    ProfitRecord countProfitForMonth(int userId, int year, String month) throws DatabaseException;


    class Request extends DAORequest {
        public Request(String query) throws SQLException {
            super(query);
        }

        public Request() throws SQLException {
            super();
        }
    }
}
