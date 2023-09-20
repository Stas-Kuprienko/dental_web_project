package dental.domain.userset;

import dental.domain.data_structures.MyList;
import dental.domain.works.ProductMapper;
import dental.domain.works.WorkRecord;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

public class Account implements Serializable {

    private int id;

    private String name;

    private String login;

    private byte[] password;

    private final LocalDate created;

    public final ProductMapper productMap;

    /**
     * The {@link MyList list} of the unclosed {@link WorkRecord} objects for account.
     */
    public final MyList<WorkRecord> workRecords;


    /**
     * Create a new Account object.
     * @param name     The user name.
     * @param login    Login for the user authorization.
     * @param password The password for access to the Account
     */
    public Account(String name, String login, String password) {
        this.created = LocalDate.now();
        this.productMap = new ProductMapper();
        this.workRecords = new MyList<>();
        if ((name == null || name.isEmpty())
          || (login == null || login.isEmpty())
          || (password == null || password.isEmpty())) {
            return;
        }
        this.name = name;
        this.login = login;
        this.password = Authenticator.passwordHash(password);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Account account = (Account) o;
        return Objects.equals(name, account.name)
                && Objects.equals(login, account.login)
                && Objects.equals(created, account.created);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, created);
    }

    @Override
    public String toString() {
        return "Account{" + "name='" + name + '\'' +
                ", login='" + login + '\'' +
                ", created=" + created +
                '}';
    }


    /*                           ||
            Getters and setters  \/
     */

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public byte[] getPassword() {
        return password;
    }

    public LocalDate getCreated() {
        return created;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

}
