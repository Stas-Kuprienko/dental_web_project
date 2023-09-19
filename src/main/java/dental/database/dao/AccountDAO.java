package dental.database.dao;

import dental.database.service.DBConfig;
import dental.domain.data_structures.MyList;
import dental.domain.userset.Account;
import dental.domain.userset.Authenticator;

import javax.sql.rowset.serial.SerialBlob;
import java.lang.reflect.Constructor;
import java.sql.*;

public class AccountDAO implements DAO<Account> {

    private AccountDAO() {}
    static {
        instance = new AccountDAO();
    }

    private static final AccountDAO instance;
    public static final String TABLE_NAME = DBConfig.DATA_BASE + ".account";

    private static final String ACCOUNT_FIELDS = "name, login, password, created";

    @Override
    public boolean insert(Account account) throws Exception {
        String injectionCounts = "?, ".repeat(ACCOUNT_FIELDS.split(",").length);
        injectionCounts = injectionCounts.substring(0, injectionCounts.length() - 2);
        String query = String.format(SQL_Sample.INSERT.QUERY, TABLE_NAME, ACCOUNT_FIELDS, injectionCounts);
        DBRequest request = null;
        boolean isSuccess;
        try {
            request = new DBRequest(query);
            PreparedStatement statement = request.getStatement();
            statement.setString(1, account.getName());
            statement.setString(2, account.getLogin());
            statement.setBlob(3, new SerialBlob(account.getPassword()));
            statement.setDate(4, Date.valueOf(account.getCreated()));
            statement.executeUpdate();
            isSuccess = DBRequest.setID(account, statement);
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        } finally {
            if (request != null) {
                request.close();
            }
        }
        return isSuccess;
    }

    @Override
    public MyList<Account> getAll() {
        return null;
    }

    @Override
    public Account get(int id) {
        return null;
    }

    public Account get(String login, String password) throws SQLException {
        String where = "login = ? AND password = ?";
        byte[] passwordBytes = Authenticator.passwordHash(password);
        String query = String.format(SQL_Sample.SELECT_WHERE.QUERY, "*", TABLE_NAME, where);
        DBRequest request = new DBRequest(query);
        ResultSet resultSet;
        Account account;
        try {
            request.getStatement().setString(1, login);
            request.getStatement().setBlob(2, new SerialBlob(passwordBytes));
            resultSet = request.getStatement().executeQuery();
            account = new AccountInstantiation(resultSet).accounts.get(0);
        } catch (Exception e) {
            throw new SQLException(e.getCause());
        } finally {
            request.close();
        }
        return account;
    }

    @Override
    public boolean remove(int id) throws SQLException {
        String query = String.format(SQL_Sample.DELETE.QUERY, TABLE_NAME, "id = ?");
        boolean isSuccess;
        DBRequest request = null;
        try {
            request = new DBRequest(query);
            request.getStatement().setInt(1, id);
            isSuccess = request.getStatement().execute();
        } catch (SQLException e) {
            throw new SQLException(e);
        } finally {
            if (request != null) {
                request.close();
            }
        }
        return isSuccess;
    }

    public static synchronized AccountDAO getInstance() {
        return instance;
    }

    @SuppressWarnings("all")
    private class AccountInstantiation extends Instantiation<Account> {

        private final ResultSet resultSet;
        private final MyList<Account> accounts;
        private final Constructor<Account> constructor;

        private AccountInstantiation(ResultSet resultSet) throws Exception {
            try {
                accounts = new MyList<>();
                this.resultSet = resultSet;
                constructor = Account.class.getDeclaredConstructor();
                constructor.setAccessible(true);
                build();
                constructor.setAccessible(false);
            } catch (Exception e) {
                throw new Exception(e.getCause());
            } finally {
                resultSet.close();
            }
        }

        @Override
        protected void build() throws Exception {
            String ID = "id";
            String NAME = "name";
            String LOGIN = "login";
            String PASSWORD = "password";
            String CREATED = "created";
            while (resultSet.next()) {
                Account account = constructor.newInstance();
                account.setName(resultSet.getString(NAME));
                account.setLogin(resultSet.getString(LOGIN));
                Blob blobPassword = resultSet.getBlob(PASSWORD);
                byte[] password = blobPassword.getBinaryStream().readAllBytes();
                setObjectPrivateField(account, PASSWORD, password);
                setObjectPrivateField(account, CREATED, resultSet.getDate(CREATED).toLocalDate());
                setObjectPrivateField(account, ID, resultSet.getInt(ID));
                accounts.add(account);
            }
        }
    }
}
