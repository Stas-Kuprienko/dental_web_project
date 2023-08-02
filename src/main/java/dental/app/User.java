package dental.app;

public class User {

    private User() {}

    private static Account account;

    public static Account getAccount() {
        return account;
    }

    public static void setAccount(Account account) {
        User.account = account;
    }
}
