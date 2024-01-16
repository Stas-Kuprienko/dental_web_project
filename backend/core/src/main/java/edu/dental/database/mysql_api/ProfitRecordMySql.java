package edu.dental.database.mysql_api;

import edu.dental.database.DatabaseException;
import edu.dental.database.dao.ProfitRecordDAO;
import edu.dental.entities.ProfitRecord;
import utils.collections.SimpleList;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ProfitRecordMySql implements ProfitRecordDAO, MySQL_DAO {

    ProfitRecordMySql() {}

    @Override
    public ProfitRecord[] countAllProfits(int userId) throws DatabaseException {
        try (Request request = new Request(MySqlSamples.ALL_PROFITS.QUERY)) {
            request.getPreparedStatement().setInt(1, userId);
            ResultSet resultSet = request.getPreparedStatement().executeQuery();
            SimpleList<ProfitRecord> records = instantiating(resultSet);
            return records.toArray(new ProfitRecord[]{});
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    @Override
    public ProfitRecord countProfitForMonth(int userId, int year, String month) throws DatabaseException {
        try (Request request = new Request(MySqlSamples.MONTH_PROFIT.QUERY)) {
            PreparedStatement statement = request.getPreparedStatement();
            statement.setInt(1, userId);
            statement.setInt(2, year);
            statement.setString(3, month);
            ResultSet resultSet = statement.executeQuery();
            return instantiating(resultSet).get(0);
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }


    private SimpleList<ProfitRecord> instantiating(ResultSet resultSet) throws SQLException {
        try (resultSet) {
            SimpleList<ProfitRecord> records = new SimpleList<>();
            while (resultSet.next()) {
                String month = resultSet.getString(1);
                int year = resultSet.getInt(2);
                int amount = resultSet.getInt(3);
                ProfitRecord salary = new ProfitRecord(year, month, amount);
                records.add(salary);
            }
            return records;
        }
    }
}
