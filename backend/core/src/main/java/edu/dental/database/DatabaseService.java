package edu.dental.database;

import edu.dental.database.dao.*;
import edu.dental.domain.APIManager;

public interface DatabaseService {

    TableInitializer getTableInitializer();

    UserDAO getUserDAO();

    ProductMapDAO getProductMapDAO(int userId);

    DentalWorkDAO getDentalWorkDAO();

    ProductDAO getProductDAO(int workId);

    ProfitRecordDAO getProfitRecordDAO();

    static DatabaseService getInstance() {
        return APIManager.INSTANCE.getDatabaseService();
    }
}
