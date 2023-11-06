package edu.dental.database;

import edu.dental.database.dao.*;
import edu.dental.domain.entities.User;

public interface DBService {

    TableInitializer getTableInitializer();

    User authenticate(String login, String password) throws DatabaseException;

    UserDAO getUserDAO();

    ProductMapDAO getProductMapDAO(User user);

    DentalWorkDAO getDentalWorkDAO(User user);

    ProductDAO getProductDAO(int workId);

}
