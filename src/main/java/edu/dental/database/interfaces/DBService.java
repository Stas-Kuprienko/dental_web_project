package edu.dental.database.interfaces;

import edu.dental.domain.entities.User;

public interface DBService {

    User authenticate(String login, String password);

    DAO<?> getUserDAO();

    DAO<?> getWorkRecordDAO();

    DAO<?> getProductDAO();

    TablesCreator getTablesCreator();

}
