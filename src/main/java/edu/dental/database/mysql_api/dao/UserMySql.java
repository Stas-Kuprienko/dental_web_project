package edu.dental.database.mysql_api.dao;

import edu.dental.database.connection.DBConfiguration;
import edu.dental.database.interfaces.DAO;
import edu.dental.domain.authentication.Authenticator;
import edu.dental.domain.entities.User;
import edu.dental.utils.data_structures.MyList;

import javax.sql.rowset.serial.SerialBlob;
import java.io.IOException;
import java.sql.*;
import java.util.Collection;

public class UserMySql implements DAO<User> {

    public static final String TABLE = DBConfiguration.DATA_BASE + ".user";

    private static final String FIELDS = "id, name, login, password, created";

    @Override
    public boolean put(User object) throws SQLException {
        String injections = "?, ".repeat(FIELDS.split(", ").length - 1);
        injections = "DEFAULT, " + injections.substring(0, injections.length() - 2);
        String query = String.format(MySqlSamples.INSERT.QUERY, TABLE, FIELDS, injections);
        boolean isDone = false;
        try (Request request = new Request(query)) {
            byte i = 2;
            PreparedStatement statement = request.getStatement();
            statement.setString(i++, object.getName());
            statement.setString(i++, object.getLogin());
            statement.setBlob(i++, new SerialBlob(object.getPassword()));
            statement.setDate(i, Date.valueOf(object.getCreated()));
            statement.executeUpdate();
            isDone = request.setID(object, statement);
        } catch (SQLException e) {
            //TODO logger
            throw e;
        }

        return isDone;
    }

    @Override
    public Collection<User> getAll() throws SQLException {
        String query = String.format(MySqlSamples.SELECT_ALL.QUERY, "*", TABLE);
        MyList<User> usersList;
        try (Request request = new Request(query)) {
            ResultSet resultSet = request.getStatement().executeQuery();
            usersList = (MyList<User>) new UserInstantiation(resultSet).build();
        } catch (SQLException e) {
            //TODO logger
            throw e;
        }
        return usersList;
    }

    @Override
    public User get(int id) throws SQLException {
        String where = "id = ?";
        String query = String.format(MySqlSamples.SELECT_WHERE.QUERY, "*", TABLE, where);
        ResultSet resultSet;
        User user;
        try (Request request = new Request(query)) {
            request.getStatement().setInt(1, id);
            resultSet = request.getStatement().executeQuery();
            MyList<User> list = (MyList<User>) new UserInstantiation(resultSet).build();
            user = list.get(0);
        } catch (SQLException | NullPointerException e) {
            throw new SQLException(e.getCause());
        }
        return user;
    }

    @Override
    public User search(Object value1, Object value2) throws SQLException {
        String where = "login = ? AND password = ?";
        byte[] password = Authenticator.passwordHash((String) value2);
        String query = String.format(MySqlSamples.SELECT_WHERE.QUERY, "*", TABLE, where);
        ResultSet resultSet;
        User user;
        try (Request request = new Request(query)) {
            request.getStatement().setString(1, (String) value1);
            request.getStatement().setBlob(2, new SerialBlob(password));
            resultSet = request.getStatement().executeQuery();
            MyList<User> result = (MyList<User>) new UserInstantiation(resultSet).build();
            return result.get(0);
        } catch (SQLException e) {
            //TODO logger
            throw e;
        }
    }

    @Override
    public boolean edit(User object) {
        return false;
    }

    @Override
    public boolean delete(int id) throws SQLException {
        String query = String.format(MySqlSamples.DELETE.QUERY, TABLE, "id = ?");
        try (Request request = new Request(query)) {
            request.getStatement().setInt(1, id);
            return request.getStatement().execute();
        } catch (SQLException e) {
            //TODO logger
            throw e;
        }
    }

    protected static class UserInstantiation implements Instantiating<User> {

        private final MyList<User> usersList;
        private final ResultSet resultSet;

        protected UserInstantiation(ResultSet resultSet) {
            this.usersList = new MyList<>();
            this.resultSet = resultSet;
        }

        @Override
        public Collection<User> build() throws SQLException {
            try (resultSet) {
                while (resultSet.next()) {
                    User user = new User();
                    user.setId(resultSet.getInt("id"));
                    user.setName(resultSet.getString("name"));
                    user.setLogin(resultSet.getString("login"));
                    Blob blob = resultSet.getBlob("password");
                    byte[] password = blob.getBinaryStream().readAllBytes();
                    user.setPassword(password);
                    user.setCreated(resultSet.getDate("created").toLocalDate());
                    usersList.add(user);
                }
            } catch (SQLException | IOException e) {
                //TODO logger
                throw new SQLException(e.getCause());
            }
            return this.usersList;
        }
    }

}
