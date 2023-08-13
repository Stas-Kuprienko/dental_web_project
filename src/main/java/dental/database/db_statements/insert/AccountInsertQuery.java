package dental.database.db_statements.insert;

import dental.app.userset.Account;
import dental.database.db_statements.IQuery;

import java.sql.SQLException;

public class AccountInsertQuery extends IQuery {

    final Account account;

    final int accountID;
    final String login;

    final String SAMPLE = "INSERT INTO accounts (id, login) VALUES (%s, '%s');";

    public AccountInsertQuery(Account account) throws SQLException {
        this.account = account;
        this.accountID = account.hashCode();
        this.login = account.getLogin();

        String query = String.format(SAMPLE, accountID, login);

        doQuery(query);
    }
}
