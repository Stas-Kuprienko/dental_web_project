package edu.dental.database.mysql_api;

import edu.dental.database.DatabaseService;
import edu.dental.database.DatabaseException;
import edu.dental.database.TableInitializer;
import edu.dental.database.dao.*;
import edu.dental.domain.entities.SalaryRecord;
import edu.dental.domain.entities.User;
import utils.collections.SimpleList;

import java.sql.ResultSet;
import java.sql.SQLException;

public class MyDatabaseService implements DatabaseService {

    private MyDatabaseService() {}

    @Override
    public TableInitializer getTableInitializer() {
        return new MySqlInitializer();
    }

    public User authenticate(String login, String password) throws DatabaseException {
        return new UserMySql().search(login, password).get(0);
    }

    @Override
    public UserDAO getUserDAO() {
        return new UserMySql();
    }

    @Override
    public ProductMapDAO getProductMapDAO(User user) {
            return new ProductMapMySql(user);
    }

    @Override
    public DentalWorkDAO getDentalWorkDAO() {
            return new DentalWorkMySql();
    }

    @Override
    public ProductDAO getProductDAO(int workId) {
            return new ProductMySql(workId);
    }

    @Override
    public SalaryRecord[] countAllSalaries(User user) throws DatabaseException {
        SimpleList<SalaryRecord> records = new SimpleList<>();
        try (DAO.Request request = new DAO.Request(MySqlSamples.ALL_SALARIES.QUERY)) {
            request.getPreparedStatement().setInt(1, user.getId());
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
