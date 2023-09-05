package dental.database.requests.insert;

import dental.app.userset.Account;
import dental.database.requests.PushRequest;

import java.sql.SQLException;

public class AccountInsertRequest extends PushRequest {

    final Account account;

    final int accountID;
    final String login;

    final String SAMPLE = "INSERT INTO accounts (id, login) VALUES (%s, '%s');";

    public AccountInsertRequest(Account account) throws SQLException {
        this.account = account;
        this.accountID = account.hashCode();
        this.login = account.getLogin();

        String request = String.format(SAMPLE, accountID, login);

        doRequest(request);
    }
}
