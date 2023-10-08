package edu.dental.database.interfaces;

import edu.dental.database.DatabaseException;
import edu.dental.domain.entities.User;

public interface DBService {

    User authenticate(String login, String password) throws DatabaseException;

    UserDAO getUserDAO();

    ProductMapperDAO getProductMapperDAO(Object... args) throws DatabaseException;

    WorkRecordDAO getWorkRecordDAO(Object... args) throws DatabaseException;

    ProductDAO getProductDAO(Object... args) throws DatabaseException;

    TablesCreator getTablesCreator();

}
