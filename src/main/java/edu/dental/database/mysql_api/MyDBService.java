package edu.dental.database.mysql_api;

import edu.dental.database.DBService;
import edu.dental.database.DatabaseException;
import edu.dental.database.dao.ProductDAO;
import edu.dental.database.dao.ProductMapDAO;
import edu.dental.database.dao.UserDAO;
import edu.dental.database.dao.DentalWorkDAO;
import edu.dental.database.mysql_api.dao.ProductMapMySql;
import edu.dental.database.mysql_api.dao.ProductMySql;
import edu.dental.database.mysql_api.dao.UserMySql;
import edu.dental.database.mysql_api.dao.DentalWorkMySql;
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
    public ProductMapDAO getProductMapDAO(User user) {
            return new ProductMapMySql(user);
    }

    @Override
    public DentalWorkDAO getDentalWorkDAO(User user) {
            return new DentalWorkMySql(user);
    }

    @Override
    public ProductDAO getProductDAO(int workId) {
            return new ProductMySql(workId);
    }

    public static synchronized DBService getInstance() {
        return instance;
    }

}
