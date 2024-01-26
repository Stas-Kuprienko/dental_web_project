package edu.dental.domain.user.my_user_service;

import edu.dental.database.DatabaseException;
import edu.dental.database.DatabaseService;
import edu.dental.domain.user.AccountException;
import edu.dental.domain.user.UserService;
import edu.dental.entities.User;

public class MyUserService implements UserService {

    private final DatabaseService databaseService;

    private MyUserService() {
        this.databaseService = DatabaseService.getInstance();
    }


    @Override
    public User create(String name, String login, byte[] password) throws AccountException {
        User user = new User(name, login, password);
        try {
            if (databaseService.getUserDAO().put(user)) {
                return user;
            } else throw new AccountException(AccountException.CAUSE.SERVER_ERROR, AccountException.MESSAGE.DATABASE_ERROR);
        } catch (DatabaseException e) {
            throw new AccountException(AccountException.CAUSE.SERVER_ERROR, e);
        }
    }

    @Override
    public User findUser(String login) throws AccountException {
        try {
            return databaseService.getUserDAO().search(login).get(0);
        } catch (DatabaseException e) {
            if (e.getMessage().equals("The such object is not found.")) {
                return null;
            } else {
                throw new AccountException(AccountException.CAUSE.SERVER_ERROR, AccountException.MESSAGE.DATABASE_ERROR);
            }
        }
    }

    @Override
    public User get(int id) throws AccountException {
        try {
            return databaseService.getUserDAO().get(id);
        } catch (DatabaseException e) {
            throw new AccountException(AccountException.CAUSE.SERVER_ERROR, e);
        }
    }

    @Override
    public boolean update(User user) throws AccountException {
        try {
            return databaseService.getUserDAO().update(user);
        } catch (DatabaseException e) {
            throw new AccountException(AccountException.CAUSE.SERVER_ERROR, e);
        }
    }

    @Override
    public boolean deleteUser(User user) throws AccountException {
        try {
            return databaseService.getUserDAO().delete(user.getId());
        } catch (DatabaseException e) {
            throw new AccountException(AccountException.CAUSE.SERVER_ERROR, e);
        }
    }
}
