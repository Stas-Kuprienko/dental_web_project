package dental.app;

import java.util.HashMap;
import java.util.Scanner;

public class User {

    private User() {}

    static {

        //TODO register the reading of accounts from files.
        accounts = new HashMap<>();

        account = null;
    }

    /**
     * The map of all existent {@link Account} objects.
     * The accounts' login used as the {@linkplain HashMap#get(Object) key}.
     */
    private final static HashMap<String, Account> accounts;

    /**
     * The variable of the current user {@link Account}.
     */
    private static Account account;


    /**
     * The user {@link Account account} logging in system.
     * @param scanner The {@link Scanner} objects for input.
     * @return The logged Account object.
     */
    private static Account logIn(Scanner scanner) {
        //TODO correct method for the web.

        String login;
        String password;
        Account acc;

        do {
            System.out.print("Enter your login: ");
            login = scanner.nextLine();
            acc = accounts.get(login);
            if (acc == null) {
                System.out.println("The account is not found! Try input your login.");
            }
        } while (acc == null);

        do {
            System.out.print("Enter your password: ");
            password = scanner.nextLine();
            account = acc.verifyPassword(password.getBytes());
            if (account == null) {
                System.out.println("Incorrect password! Please try again.");
            }
        } while (account == null);
        return account;
    }


    /*                           ||
            Getters and setters  \/
     */

    public static Account getAccount() {
        return account;
    }
    public static void setAccount(Account account) {
        User.account = account;
    }
}
