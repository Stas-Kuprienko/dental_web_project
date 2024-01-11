package edu.dental.database.mysql_api;

import edu.dental.database.DatabaseException;
import edu.dental.database.dao.SalaryRecordDAO;
import edu.dental.entities.SalaryRecord;
import utils.collections.SimpleList;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SalaryRecordMySql implements SalaryRecordDAO {

    SalaryRecordMySql() {}

    @Override
    public SalaryRecord[] countAllSalaries(int userId) throws DatabaseException {
        //TODO
        try (Request request = new Request(MySqlSamples.ALL_SALARIES.QUERY)) {
            request.getPreparedStatement().setInt(1, userId);
            ResultSet resultSet = request.getPreparedStatement().executeQuery();
            SimpleList<SalaryRecord> records = instantiating(resultSet);
            return records.toArray(new SalaryRecord[]{});
        } catch (SQLException e) {
            throw new DatabaseException(e.getMessage(), e.fillInStackTrace());
        }
    }

    @Override
    public SalaryRecord countSalaryForMonth(int userId, int year, String month) throws DatabaseException {
        try (Request request = new Request(MySqlSamples.MONTH_SALARY.QUERY)) {
            PreparedStatement statement = request.getPreparedStatement();
            statement.setInt(1, userId);
            statement.setInt(2, year);
            statement.setString(3, month);
            ResultSet resultSet = statement.executeQuery();
            return instantiating(resultSet).get(0);
        } catch (SQLException e) {
            throw new DatabaseException(e.getMessage(), e.fillInStackTrace());
        }
    }


    private SimpleList<SalaryRecord> instantiating(ResultSet resultSet) throws SQLException {
        try (resultSet) {
            SimpleList<SalaryRecord> records = new SimpleList<>();
            while (resultSet.next()) {
                String month = resultSet.getString(1);
                int year = resultSet.getInt(2);
                int amount = resultSet.getInt(3);
                SalaryRecord salary = new SalaryRecord(year, month, amount);
                records.add(salary);
            }
            return records;
        }
    }
}
