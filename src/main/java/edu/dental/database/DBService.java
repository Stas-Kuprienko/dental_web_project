package edu.dental.database;

import edu.dental.database.dao.*;
import edu.dental.domain.entities.User;

public interface DBService {

    User authenticate(String login, String password) throws DatabaseException;

    UserDAO getUserDAO();

    ProductMapDAO getProductMapDAO(User user);

    WorkRecordDAO getWorkRecordDAO(User user);

    ProductDAO getProductDAO(int workId);

}
