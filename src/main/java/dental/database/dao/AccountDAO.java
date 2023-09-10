package dental.database.dao;

import dental.app.MyList;
import dental.app.userset.Account;
import dental.database.DBConfig;

import javax.sql.rowset.serial.SerialBlob;
import java.lang.reflect.Constructor;
import java.sql.*;

public class AccountDAO implements DAO<Account> {

    private AccountDAO() {}
    static {
        instance = new AccountDAO();
    }

    private static final AccountDAO instance;
    private static final String TABLE_NAME = "account";


    @Override
    public void add(Account account) throws SQLException {
        String query = String.format("INSERT INTO %s.%s (name, login, password, created) VALUES (?, ?, ?, ?);", DBConfig.DATA_BASE, TABLE_NAME);
        DBRequest request = new DBRequest(query);
        PreparedStatement statement = request.getStatement();
        statement.setString(1, account.getName());
        statement.setString(2, account.getLogin());
        statement.setBlob(3, new SerialBlob(account.getPassword()));
        statement.setDate(4, Date.valueOf(account.getCreated()));
        statement.execute();
        request.close();
    }

    @Override
    public MyList<Account> getAll() throws Exception {
        String query = String.format("SELECT * FROM %s.%s;", DBConfig.DATA_BASE, TABLE_NAME);
        DBRequest request = new DBRequest(query);
        ResultSet resultSet = request.getStatement().getResultSet();
        MyList<Account> accounts = new AccountInstantiation(resultSet).getAccounts();
        request.close();
        return accounts;
    }

    @Override
    public Account get(int id) throws Exception {
        String query = String.format("SELECT * FROM %s.%s WHERE id = ?;", DBConfig.DATA_BASE, TABLE_NAME);
        DBRequest request = new DBRequest(query);
        request.getStatement().setInt(1, id);
        ResultSet resultSet = request.getStatement().executeQuery();
        Account account = new AccountInstantiation(resultSet).getAccounts().get(0);
        request.close();
        //TODO
        return account;
    }

    @Override
    public void remove(int id) throws SQLException {
        String query = String.format("DELETE FROM %s.%s WHERE id = ?;", DBConfig.DATA_BASE, TABLE_NAME);
        DBRequest request = new DBRequest(query);
        request.getStatement().setInt(1, id);
        request.close();
    }

    @Override
    public void remove(Account account) throws SQLException {
        remove(account.getId());
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

        protected MyList<Account> getAccounts() {
            return accounts;
        }
    }
}
