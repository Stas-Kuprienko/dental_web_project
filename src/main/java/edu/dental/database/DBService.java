package edu.dental.database;

import edu.dental.database.dao.*;
import edu.dental.domain.entities.User;

public interface DBService {

    User authenticate(String login, String password) throws DatabaseException;

    UserDAO getUserDAO();

    ProductMapDAO getProductMapperDAO(Object... args) throws DatabaseException;

    WorkRecordDAO getWorkRecordDAO(User user) throws DatabaseException;

    ProductDAO getProductDAO(Object... args) throws DatabaseException;

}
