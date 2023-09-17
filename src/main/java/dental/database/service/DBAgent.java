package dental.database.service;

import dental.database.dao.AccountDAO;
import dental.domain.userset.Account;

import java.sql.SQLException;

/**
 * Class to connect domain layer and database layer.
 */
public final class DBAgent {


    public static Account authenticate(String login, String password) {
        Account account = null;
        try {
            account = AccountDAO.getInstance().get(login, password);
        } catch (SQLException e) {
            //TODO
        }
        return account;
    }

}
