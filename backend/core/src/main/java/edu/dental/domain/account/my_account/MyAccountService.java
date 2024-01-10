package edu.dental.domain.account.my_account;

import edu.dental.database.DatabaseException;
import edu.dental.database.DatabaseService;
import edu.dental.database.dao.UserDAO;
import edu.dental.domain.APIManager;
import edu.dental.domain.account.AccountException;
import edu.dental.domain.account.AccountService;
import edu.dental.entities.User;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MyAccountService implements AccountService {

    private MyAccountService() {}


    /**
     * Verification the user's password when logging in.
     * @param password The user's password.
     * @return The {@link User} object if verification was successful, or null if not.
     */
    @Override
    public boolean verification(User user, String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            return MessageDigest.isEqual(user.getPassword(), md.digest(password.getBytes()));
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Verification the user's encoded {@link MessageDigest MD5} password bytes.
     * @param password The user's password, encoded MD5.
     * @return The {@link User} object if verification was successful, or null if not.
     */
    @Override
    public boolean verification(User user, byte[] password) {
        return MessageDigest.isEqual(user.getPassword(), password);
    }

    /**
     * Get the {@link MessageDigest MD5} hash of the password.
     * @param password The user's password string.
     * @return byte array of the password hash.
     */
    @Override
    public byte[] passwordHash(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            return md.digest(password.getBytes());
        } catch (NoSuchAlgorithmException e) {
            //TODO logger
            throw new RuntimeException(e);
        }
    }

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
        if (!verification(user, password)) {
            throw new AccountException();
        }
        return user;
    }

    @Override
    public User get(int id) throws AccountException {
        try {
            return DatabaseService.getInstance().getUserDAO().get(id);
        } catch (DatabaseException e) {
            throw new AccountException();
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
            if (verification(user, user.getPassword())) {
                byte[] passHash = passwordHash(newPassword);
                user.setPassword(passHash);
                return DatabaseService.getInstance().getUserDAO().update(user);
            } else return false;
        } catch (DatabaseException e) {
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
