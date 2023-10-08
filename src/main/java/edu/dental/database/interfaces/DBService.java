package edu.dental.database.interfaces;

import edu.dental.database.DatabaseException;
import edu.dental.domain.entities.User;

public interface DBService {

    User authenticate(String login, String password) throws DatabaseException;

    DAO<?> getUserDAO();

    DAO<?> getProductMapperDAO(Object... args) throws DatabaseException;

    DAO<?> getWorkRecordDAO(Object... args) throws DatabaseException;

    DAO<?> getProductDAO(Object... args) throws DatabaseException;

    TablesCreator getTablesCreator();

}
