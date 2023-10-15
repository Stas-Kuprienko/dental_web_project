package edu.dental.database.mysql_api;

import edu.dental.database.DBService;
import edu.dental.database.DatabaseException;
import edu.dental.database.dao.ProductDAO;
import edu.dental.database.dao.ProductMapDAO;
import edu.dental.database.dao.UserDAO;
import edu.dental.database.dao.WorkRecordDAO;
import edu.dental.database.mysql_api.dao.ProductMapMySql;
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
    public UserDAO getUserDAO() {
        return new UserMySql();
    }

    @Override
    public ProductMapDAO getProductMapperDAO(Object... args) throws DatabaseException {
        try {
            return new ProductMapMySql((User) args[0]);
        } catch (ArrayIndexOutOfBoundsException | NullPointerException | ClassCastException e) {
            throw new DatabaseException(e.getMessage(), e.getCause());
        }
    }

    @Override
    public WorkRecordDAO getWorkRecordDAO(Object... args) throws DatabaseException {
        try {
            if (args.length > 1) {
                return new WorkRecordMySql((User) args[0], (String) args[1]);
            } else {
                return new WorkRecordMySql((User) args[0]);
            }
        } catch (ArrayIndexOutOfBoundsException | NullPointerException | ClassCastException e) {
            throw new DatabaseException(e.getMessage(), e.getCause());
        }
    }

    @Override
    public ProductDAO getProductDAO(Object... args) throws DatabaseException {
        try {
            return new ProductMySql((Integer) args[0], (Integer) args[1]);
        } catch (ArrayIndexOutOfBoundsException | NullPointerException | ClassCastException e) {
            throw new DatabaseException(e.getMessage(), e.getCause());
        }
    }

    public static synchronized DBService getInstance() {
        return instance;
    }

}
