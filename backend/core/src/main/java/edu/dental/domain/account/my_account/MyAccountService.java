package edu.dental.domain.account.my_account;

import edu.dental.database.DatabaseException;
import edu.dental.database.DatabaseService;
import edu.dental.database.dao.UserDAO;
import edu.dental.domain.APIManager;
import edu.dental.domain.account.AccountException;
import edu.dental.domain.account.AccountService;
import edu.dental.domain.authentication.AuthenticationException;
import edu.dental.domain.authentication.Authenticator;
import edu.dental.entities.User;

public class MyAccountService implements AccountService {

    @Override
    public User create(String name, String login, String password) throws AccountException {
        User user = new User(name, login, password);
        try {
            if (APIManager.INSTANCE.getDatabaseService().getUserDAO().put(user)) {
                return user;
            } else throw new AccountException();
        } catch (DatabaseException e) {
            throw new AccountException();
        }
    }

    /**
     * The user authentication in system.
     * @param login    The user's account login.
     * @param password The password to verify.
     * @return The {@link User} object, if authentication was successful.
     * @throws AccountException Causes of throwing
     *  - specified user is not found;
     *  - Database exception;
     *  - incorrect password;
     *  - a given argument is null.
     */
    @Override
    public User get(String login, String password) throws AccountException {
        if ((login == null || login.isEmpty())||(password == null || password.isEmpty())) {
            throw new AccountException();
        }
        User user;
        DatabaseService databaseService = DatabaseService.getInstance();
        try {
            user = databaseService.findUser(login);
        } catch (DatabaseException e) {
            throw new AccountException();
        }
        if (user == null) {
            throw new AccountException();
        }
        if (!(Authenticator.verification(user, password))) {
            throw new AccountException();
        }
        return user;
    }

    @Override
    public User get(String token) throws AccountException {
        try {
            int id = Authenticator.verifyTokenToId(token);
            if (id <= 0) {
                throw new AccountException();
            } else {
                return DatabaseService.getInstance().getUserDAO().get(id);
            }
        } catch (DatabaseException e) {
            throw new AccountException();
        } catch (AuthenticationException e) {
            throw new AccountException(e);
        }
    }

    @Override
    public boolean update(User user) throws AccountException {
        UserDAO dao = DatabaseService.getInstance().getUserDAO();
        try {
            return dao.update(user);
        } catch (DatabaseException e) {
            throw new AccountException();
        }
    }

    @Override
    public boolean updatePassword(User user, String newPassword) throws AccountException {
        byte[] oldValue = user.getPassword();
        try {
            Authenticator.updatePassword(user, newPassword);
            return DatabaseService.getInstance().getUserDAO().update(user);
        } catch (AuthenticationException | DatabaseException e) {
            user.setPassword(oldValue);
            throw new AccountException(e);
        }
    }

    @Override
    public boolean deleteUser(User user) throws AccountException {
        UserDAO dao = DatabaseService.getInstance().getUserDAO();
        try {
            return dao.delete(user.getId());
        } catch (DatabaseException e) {
            throw new AccountException(e);
        }
    }
}
