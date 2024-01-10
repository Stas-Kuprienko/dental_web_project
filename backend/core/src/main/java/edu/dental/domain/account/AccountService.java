package edu.dental.domain.account;

import edu.dental.domain.APIManager;
import edu.dental.entities.User;

public interface AccountService {

    static AccountService getInstance() {
        return APIManager.INSTANCE.getAccountService();
    }

    boolean verification(User user, String password);

    boolean verification(User user, byte[] password);

    byte[] passwordHash(String password);

    User create(String name, String login, String password) throws AccountException;

    User get(String login, String password) throws AccountException;

    User get(int id) throws AccountException;

    boolean update(User user) throws AccountException;

    boolean updatePassword(User user, String newPassword) throws AccountException;

    boolean deleteUser(User user) throws AccountException;
}
