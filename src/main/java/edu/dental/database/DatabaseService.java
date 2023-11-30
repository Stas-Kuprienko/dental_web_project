package edu.dental.database;

import edu.dental.database.dao.*;
import edu.dental.domain.APIManager;
import edu.dental.domain.entities.SalaryRecord;
import edu.dental.domain.entities.User;

public interface DatabaseService {

    static DatabaseService getInstance() {
        return APIManager.INSTANCE.getDatabaseService();
    }

    TableInitializer getTableInitializer();

    User authenticate(String login, String password) throws DatabaseException;

    UserDAO getUserDAO();

    ProductMapDAO getProductMapDAO(User user);

    DentalWorkDAO getDentalWorkDAO(User user);

    ProductDAO getProductDAO(int workId);

    SalaryRecord[] countAllSalaries(User user) throws DatabaseException;
}
