package edu.dental.database;

import edu.dental.database.dao.*;
import edu.dental.domain.APIManager;
import edu.dental.entities.User;

public interface DatabaseService {

    TableInitializer getTableInitializer();

    User findUser(String login) throws DatabaseException;

    UserDAO getUserDAO();

    ProductMapDAO getProductMapDAO(int userId);

    DentalWorkDAO getDentalWorkDAO();

    ProductDAO getProductDAO(int workId);

    ProfitRecordDAO getProfitRecordDAO();

    static DatabaseService getInstance() {
        return APIManager.INSTANCE.getDatabaseService();
    }
}
