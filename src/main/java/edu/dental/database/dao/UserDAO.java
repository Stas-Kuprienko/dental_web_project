package edu.dental.database.dao;

import edu.dental.database.DatabaseException;
import edu.dental.domain.entities.User;

import java.util.List;

public interface UserDAO extends DAO<User> {
    @Override
    boolean putAll(List<User> list) throws DatabaseException;

    @Override
    boolean put(User object) throws DatabaseException;

    @Override
    List<User> getAll() throws DatabaseException;

    @Override
    User get(int id) throws DatabaseException;

    @Override
    List<User> search(Object... args) throws DatabaseException;

    @Override
    boolean edit(User object) throws DatabaseException;

    @Override
    boolean delete(int id) throws DatabaseException;
}
