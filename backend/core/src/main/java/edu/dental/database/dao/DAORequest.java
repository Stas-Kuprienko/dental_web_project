package edu.dental.database.dao;

import edu.dental.database.connection.ConnectionPool;
import edu.dental.entities.IDHaving;

import java.sql.*;

public class DAORequest implements IRequest {

    private Connection connection;

    private final PreparedStatement preparedStatement;

    private final Statement statement;

    public DAORequest(String query) throws SQLException {
        this.connection = ConnectionPool.get();
        try {
            this.preparedStatement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            this.statement = null;
        } catch (SQLException e) {
            //TODO loggers
            ConnectionPool.put(connection);
            throw e;
        }
    }

    public DAORequest() throws SQLException {
        this.connection = ConnectionPool.get();
        try {
            this.statement = connection.createStatement();
            this.preparedStatement = null;
        } catch (SQLException e) {
            //TODO loggers
            ConnectionPool.put(connection);
            throw e;
        }
    }

    @Override
    public boolean setID(IDHaving object) throws SQLException {
        try (ResultSet resultSet = preparedStatement.getGeneratedKeys()) {
            if (resultSet.next()) {
                object.setId((int) resultSet.getLong(1));
                return true;
            } else return false;
        }
    }

    @Override
    public Blob createBlob() throws SQLException {
        return connection.createBlob();
    }

    @Override
    public PreparedStatement getPreparedStatement() {
        return preparedStatement;
    }

    public Statement getStatement() {
        return statement;
    }

    @Override
    public void close() {
        try {
            if (preparedStatement != null) {
                preparedStatement.close();
            }
            if (statement != null) {
                statement.close();
            }
        } catch (SQLException e) {
            //TODO logger
            e.printStackTrace();
        } finally {
            ConnectionPool.put(connection);
            connection = null;
        }
    }
}