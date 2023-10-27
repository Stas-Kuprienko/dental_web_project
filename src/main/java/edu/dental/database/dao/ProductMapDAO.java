package edu.dental.database.dao;

import edu.dental.database.DatabaseException;

import java.sql.SQLException;
import java.util.Map;

public interface ProductMapDAO {

    boolean putAll(Map<String, Integer> map) throws DatabaseException;

    int put(String key, int value) throws DatabaseException;

    Map<String, Integer> get() throws DatabaseException;

    boolean edit(int id, int value) throws DatabaseException;

    boolean delete(int id) throws DatabaseException;

    class Request extends DAO.Request {

        public Request(String query) throws SQLException {
            super(query);
        }
    }
}
