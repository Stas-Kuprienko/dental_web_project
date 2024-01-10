package edu.dental.database.mysql_api;

import edu.dental.database.DatabaseException;
import edu.dental.database.TableInitializer;
import edu.dental.database.dao.Instantiation;
import edu.dental.database.dao.UserDAO;
import edu.dental.entities.User;
import utils.collections.SimpleList;

import javax.sql.rowset.serial.SerialBlob;
import java.io.IOException;
import java.sql.*;
import java.util.List;

public class UserMySql implements UserDAO {

    public static final String TABLE = TableInitializer.USER;

    public static final String FIELDS = "id, name, email, created, password";

    UserMySql() {}


    @Override
    public boolean put(User object) throws DatabaseException {
        String injections = "?, ".repeat(FIELDS.split(", ").length - 1);
        injections = "DEFAULT, " + injections.substring(0, injections.length() - 2);
        String query = String.format(MySqlSamples.INSERT.QUERY, TABLE, FIELDS, injections);
        try (Request request = new Request(query)) {
            byte i = 1;
            PreparedStatement statement = request.getPreparedStatement();
            statement.setString(i++, object.getName());
            statement.setString(i++, object.getEmail());
            statement.setDate(i++, Date.valueOf(object.getCreated()));
            statement.setBlob(i, new SerialBlob(object.getPassword()));
            statement.executeUpdate();
            return request.setID(object);
        } catch (SQLException e) {
            //TODO logger
            throw new DatabaseException(e.getMessage(), e.getCause());
        }
    }

    public List<User> getAll() throws DatabaseException {
        String query = String.format(MySqlSamples.SELECT_ALL.QUERY, "*", TABLE);
        SimpleList<User> usersList;
        try (Request request = new Request(query)) {
            ResultSet resultSet = request.getPreparedStatement().executeQuery();
            usersList = (SimpleList<User>) new UserInstantiation(resultSet).build();
        } catch (SQLException | IOException e) {
            //TODO logger
            throw new DatabaseException(e.getMessage(), e.getCause());
        }
        return usersList;
    }

    @Override
    public User get(int id) throws DatabaseException {
        String query = String.format(MySqlSamples.SELECT_WHERE.QUERY, "*", TABLE, "id = ?");
        ResultSet resultSet;
        try (Request request = new Request(query)) {
            request.getPreparedStatement().setInt(1, id);
            resultSet = request.getPreparedStatement().executeQuery();
            SimpleList<User> list = (SimpleList<User>) new UserInstantiation(resultSet).build();
            return list.get(0);
        } catch (SQLException | NullPointerException | IOException e) {
            throw new DatabaseException(e.getMessage(), e.getCause());
        }
    }

    /**
     *
     * @param login user {@linkplain User#getEmail() email}.
     * @return specified {@link User} list.
     * @throws DatabaseException
     */
    @Override
    public SimpleList<User> search(String login) throws DatabaseException {
        String where = "email = ?";
        String query = String.format(MySqlSamples.SELECT_WHERE.QUERY, "*", TABLE, where);
        ResultSet resultSet;
        try (Request request = new Request(query)) {
            request.getPreparedStatement().setString(1, login);
            resultSet = request.getPreparedStatement().executeQuery();
            return (SimpleList<User>) new UserInstantiation(resultSet).build();
        } catch (SQLException | IOException | NullPointerException e) {
            //TODO logger
            throw new DatabaseException(e.getMessage(), e.getCause());
        }
    }

    @Override
    public boolean update(User object) throws DatabaseException {
        StringBuilder sets = new StringBuilder();
        String[] fields = FIELDS.split(", ");
        for (int i = 1; i < fields.length; i++) {
            sets.append(fields[i]).append("=?,");
        } sets.deleteCharAt(sets.length()-1);
        String query = String.format(MySqlSamples.UPDATE.QUERY, TABLE, sets,"id = ?");
        try (Request request = new Request(query)) {
            byte i = 1;
            request.getPreparedStatement().setString(i++, object.getName());
            request.getPreparedStatement().setString(i++, object.getEmail());
            request.getPreparedStatement().setDate(i++, Date.valueOf(object.getCreated()));
            request.getPreparedStatement().setBlob(i++, new SerialBlob(object.getPassword()));
            request.getPreparedStatement().setInt(i, object.getId());
            return request.getPreparedStatement().executeUpdate() == 1;
        } catch (SQLException e) {
            throw new DatabaseException(e.getMessage(), e.getCause());
        }
    }

    @Override
    public boolean delete(int id) throws DatabaseException {
        String query = String.format(MySqlSamples.DELETE.QUERY, TABLE, "id = ?");
        try (Request request = new Request(query)) {
            request.getPreparedStatement().setInt(1, id);
            return request.getPreparedStatement().executeUpdate() == 1;
        } catch (SQLException e) {
            //TODO logger
            throw new DatabaseException(e.getMessage(), e.getCause());
        }
    }

    protected static class UserInstantiation implements Instantiation<User> {

        private final SimpleList<User> usersList;
        private final ResultSet resultSet;

        protected UserInstantiation(ResultSet resultSet) {
            this.usersList = new SimpleList<>();
            this.resultSet = resultSet;
        }

        @Override
        public List<User> build() throws SQLException, IOException {
            try (resultSet) {
                while (resultSet.next()) {
                    User user = new User();
                    user.setId(resultSet.getInt("id"));
                    user.setName(resultSet.getString("name"));
                    user.setEmail(resultSet.getString("email"));
                    Blob blob = resultSet.getBlob("password");
                    byte[] password = blob.getBinaryStream().readAllBytes();
                    user.setPassword(password);
                    user.setCreated(resultSet.getDate("created").toLocalDate());
                    usersList.add(user);
                }
            }
            if (this.usersList.isEmpty()) {
                throw new NullPointerException("The such object is not found.");
            }
            return this.usersList;
        }
    }

}
