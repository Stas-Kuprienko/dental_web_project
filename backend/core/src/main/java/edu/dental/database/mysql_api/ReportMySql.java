package edu.dental.database.mysql_api;

import edu.dental.database.DatabaseException;
import edu.dental.database.dao.ReportDAO;
import edu.dental.entities.SalaryRecord;
import utils.collections.SimpleList;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ReportMySql implements ReportDAO {

    ReportMySql() {}

    @Override
    public SalaryRecord[] countAllSalaries(int userId) throws DatabaseException {
        SimpleList<SalaryRecord> records = new SimpleList<>();
        try (Request request = new Request(MySqlSamples.ALL_SALARIES.QUERY)) {
            request.getPreparedStatement().setInt(1, userId);
            ResultSet resultSet = request.getPreparedStatement().executeQuery();
            while (resultSet.next()) {
                String month = resultSet.getString(1);
                int year = resultSet.getInt(2);
                int amount = resultSet.getInt(3);
                SalaryRecord salary = new SalaryRecord(year, month, amount);
                records.add(salary);
            }
            resultSet.close();
            return records.toArray(new SalaryRecord[]{});
        } catch (SQLException e) {
            throw new DatabaseException(e.getMessage(), e.fillInStackTrace());
        }
    }
}
