package edu.dental.database.mysql_api;

import edu.dental.database.DatabaseException;
import edu.dental.database.DatabaseService;
import edu.dental.database.TableInitializer;
import edu.dental.database.dao.*;
import edu.dental.entities.User;

public class MyDatabaseService implements DatabaseService {

    private MyDatabaseService() {}

    @Override
    public TableInitializer getTableInitializer() {
        return new MySqlInitializer();
    }

    @Override
    public User findUser(String login) throws DatabaseException {
        return new UserMySql().search(login).get(0);
    }

    @Override
    public UserDAO getUserDAO() {
        return new UserMySql();
    }

    @Override
    public ProductMapDAO getProductMapDAO(int userId) {
            return new ProductMapMySql(userId);
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
    public SalaryRecordDAO getSalaryRecordDAO() {
        return new SalaryRecordMySql();
    }
}
