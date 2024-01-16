package edu.dental.database.dao;

import edu.dental.database.DatabaseException;
import edu.dental.entities.User;

import java.util.List;

public interface UserDAO {

    boolean put(User object) throws DatabaseException;

    User get(int id) throws DatabaseException;

    List<User> search(String login) throws DatabaseException;

    boolean update(User object) throws DatabaseException;

    boolean delete(int id) throws DatabaseException;
}
