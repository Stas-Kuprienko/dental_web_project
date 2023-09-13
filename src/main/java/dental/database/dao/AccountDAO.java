package dental.database.dao;

import dental.app.MyList;
import dental.app.userset.Account;
import dental.app.userset.Authenticator;
import dental.database.DBConfig;

import javax.sql.rowset.serial.SerialBlob;
import java.lang.reflect.Constructor;
import java.sql.*;

public class AccountDAO implements DAO<Account> {

    private AccountDAO() {}
    static {
        instance = new AccountDAO();
    }

    //TODO
    //TODO
    //TODO

    private static final AccountDAO instance;
    public static final String TABLE_NAME = DBConfig.DATA_BASE + ".account";

    private static final String ACCOUNT_FIELDS = "name, login, password, created";

    @Override
    public boolean create(Account account) throws SQLException, NoSuchFieldException, IllegalAccessException {
        String query = String.format(SQL_DAO.INSERT.QUERY, TABLE_NAME, ACCOUNT_FIELDS, "?".repeat(ACCOUNT_FIELDS.split(",").length));
        DBRequest request = new DBRequest(query);
        PreparedStatement statement = request.getStatement();
        statement.setString(1, account.getName());
        statement.setString(2, account.getLogin());
        statement.setBlob(3, new SerialBlob(account.getPassword()));
        statement.setDate(4, Date.valueOf(account.getCreated()));
        statement.executeUpdate();
        boolean isSuccess = DBRequest.setID(account, statement);
        request.close();
        return isSuccess;
    }

    @Override
    public MyList<Account> getAll() throws Exception {
        String query = String.format(SQL_DAO.SELECT_ALL.QUERY, "*", TABLE_NAME);
        DBRequest request = new DBRequest(query);
        ResultSet resultSet = request.getStatement().executeQuery();
        MyList<Account> accounts = new AccountInstantiation(resultSet).accounts;
        request.close();
        return accounts;
    }

    public Account get(String login, String password) throws Exception {
        String where = "login = ? AND password = ?";
        byte[] passwordBytes = Authenticator.passwordHash(password);
        String query = String.format(SQL_DAO.SELECT_WHERE.QUERY, "*", TABLE_NAME, where);
        DBRequest request = new DBRequest(query);
        request.getStatement().setString(1, login);
        request.getStatement().setBlob(2, new SerialBlob(passwordBytes));
        ResultSet resultSet = request.getStatement().executeQuery();
        Account account = new AccountInstantiation(resultSet).accounts.get(0);
        request.close();
        return account;
    }

    @Override
    public boolean remove(int id) throws SQLException {
        String query = String.format(SQL_DAO.DELETE.QUERY, TABLE_NAME, "id = ?");
        DBRequest request = new DBRequest(query);
        request.getStatement().setInt(1, id);
        boolean isSuccess = request.getStatement().execute();
        request.close();
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

            accounts = new MyList<>();
            this.resultSet = resultSet;
            constructor = Account.class.getDeclaredConstructor();
            constructor.setAccessible(true);
            build();
            constructor.setAccessible(false);
            resultSet.close();
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
