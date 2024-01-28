package edu.dental.domain.user;

import edu.dental.domain.APIManager;
import edu.dental.entities.User;

public interface UserService {

    static UserService getInstance() {
        return APIManager.INSTANCE.getUserService();
    }

    User create(String name, String login, byte[] password) throws AccountException;

    User findUser(String login) throws AccountException;

    User get(int id) throws AccountException;

    boolean update(User user) throws AccountException;

    boolean deleteUser(User user) throws AccountException;
}
