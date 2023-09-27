package edu.dental.database.mysql_api;

import edu.dental.database.interfaces.DBService;
import edu.dental.database.interfaces.TablesCreator;
import edu.dental.database.interfaces.DAO;
import edu.dental.domain.entities.User;

public class MyDBService implements DBService {

    private MyDBService() {}
    static {
        instance = new MyDBService();
    }
    private static final MyDBService instance;


    public User authenticate(String login, String password) {
        //TODO
        return null;
    }

    @Override
    public DAO<?> getUserDAO() {
        return null;
    }

    @Override
    public DAO<?> getWorkRecordDAO() {
        return null;
    }

    @Override
    public DAO<?> getProductDAO() {
        return null;
    }

    @Override
    public TablesCreator getTablesCreator() {
        return null;
    }

    public static synchronized DBService getInstance() {
        return instance;
    }

}
