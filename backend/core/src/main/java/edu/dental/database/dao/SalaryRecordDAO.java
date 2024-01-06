package edu.dental.database.dao;

import edu.dental.database.DatabaseException;
import edu.dental.entities.SalaryRecord;

import java.sql.SQLException;

public interface SalaryRecordDAO {

    SalaryRecord[] countAllSalaries(int userId) throws DatabaseException;

    SalaryRecord countSalaryForMonth(int userId, String year, String month) throws DatabaseException;


    class Request extends DAORequest {
        public Request(String query) throws SQLException {
            super(query);
        }

        public Request() throws SQLException {
            super();
        }
    }
}
