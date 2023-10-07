package edu.dental.database.mysql_api;

import edu.dental.database.DatabaseException;
import edu.dental.database.interfaces.DAO;
import edu.dental.database.interfaces.DBService;
import edu.dental.database.interfaces.TablesCreator;
import edu.dental.database.mysql_api.dao.ProductMySql;
import edu.dental.database.mysql_api.dao.UserMySql;
import edu.dental.database.mysql_api.dao.WorkRecordMySql;
import edu.dental.domain.entities.User;

public class MyDBService implements DBService {

    private MyDBService() {}
    static {
        instance = new MyDBService();
    }
    private static final MyDBService instance;


    public User authenticate(String login, String password) throws DatabaseException {
        return new UserMySql().search(login, password).get(0);
    }

    @Override
    public DAO<?> getUserDAO() {
        return new UserMySql();
    }

    @Override
    public DAO<?> getWorkRecordDAO(Object... args) throws DatabaseException {
        try {
            return new WorkRecordMySql((User) args[0]);
        } catch (ArrayIndexOutOfBoundsException | NullPointerException | ClassCastException e) {
            throw new DatabaseException(e.getMessage(), e.getCause());
        }
    }

    @Override
    public DAO<?> getProductDAO(Object... args) throws DatabaseException {
        try {
            return new ProductMySql((Integer) args[0], (Integer) args[1]);
        } catch (ArrayIndexOutOfBoundsException | NullPointerException | ClassCastException e) {
            throw new DatabaseException(e.getMessage(), e.getCause());
        }
    }

    @Override
    public synchronized TablesCreator getTablesCreator() {
        return MySqlTablesCreator.instance;
    }

    public static synchronized DBService getInstance() {
        return instance;
    }

}
