package dental.database.dao;

import dental.app.MyList;
import dental.app.userset.Account;
import dental.database.DBConfig;

import javax.sql.rowset.serial.SerialBlob;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.sql.*;

public class AccountDAO implements DAO<Account> {

    private AccountDAO() {}

    static {
        instance = new AccountDAO();
    }

    private static final AccountDAO instance;


    @Override
    public void add(Account account) throws SQLException {
        String query = "INSERT INTO " + DBConfig.DATA_BASE + ".account (name, login, password, created) VALUES (?, ?, ?, ?)";
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
    public MyList<Account> getAll() {
        return null;
    }

    @Override
    public Account get(int id) throws Exception {
        String query = "SELECT * FROM " + DBConfig.DATA_BASE + ".account WHERE id = ?";
        DBRequest request = new DBRequest(query);
        request.getStatement().setInt(1, id);
        ResultSet resultSet = request.getStatement().getResultSet();
        Account account = new AccountInstantiation(resultSet).getAccount();
        request.close();
        //TODO
        return account;
    }

    @Override
    public void remove(int id) {

    }

    @Override
    public void remove(Account account) {

    }

    private Account instantiation() {
        return null;
    }

    public static AccountDAO getInstance() {
        return instance;
    }

    private class AccountInstantiation extends Instantiation<Account> {

        private final String ID = "id";
        private final String NAME = "name";
        private final String LOGIN = "login";
        private final String PASSWORD = "password";
        private final String CREATED = "created";
        private final ResultSet resultSet;
        private Account account;

        private AccountInstantiation(ResultSet resultSet) throws Exception {

            this.resultSet = resultSet;
            Constructor<Account> constructor = Account.class.getDeclaredConstructor();
            constructor.setAccessible(true);
            account = constructor.newInstance();
            build();
            constructor.setAccessible(false);
            resultSet.close();
        }

        @Override
        protected void build() throws SQLException, IOException, NoSuchFieldException, IllegalAccessException {
            while (resultSet.next()) {
                account.setName(resultSet.getString(NAME));
                account.setLogin(resultSet.getString(LOGIN));
                Blob blobPassword = resultSet.getBlob(PASSWORD);
                byte[] password = blobPassword.getBinaryStream().readAllBytes();
                setObjectPrivateField(account, PASSWORD, password);
                setObjectPrivateField(account, CREATED, resultSet.getDate(CREATED));
                setObjectPrivateField(account, ID, resultSet.getInt(ID));
                //TODO
            }
        }

        public Account getAccount() {
            return account;
        }
    }
}
