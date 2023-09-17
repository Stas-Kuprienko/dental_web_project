package dental.domain.userset;

import dental.domain.works.RecordManager;
import dental.domain.reports.TableReport;
import dental.app.filetools.Extractable;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Objects;

public class Account implements Serializable, Extractable {

    private int id;

    private String name;

    private String login;

    private byte[] password;

    private final LocalDate created;

    public final RecordManager recordManager;

    /**
     * The HashMap of the HashMap of the reports,
     *  the string year is used as the Key.
     * Value is the HashMap of the monthly reports,
     *  in which the string month is used as the Key,
     *   and value is the Report object.
     */
    private HashMap<String, HashMap<String, TableReport>> reports; //TODO remove from here

    private HashMap<String, String> reportTableTitles; //TODO rework by properties

    /**
     * Create a new Account object.
     * @param name     The user name.
     * @param login    Login for the user authorization.
     * @param password The password for access to the Account
     */
    public Account(String name, String login, String password) {
        this.created = LocalDate.now();
        this.recordManager = new RecordManager();
        if ((name == null || name.isEmpty())
          || (login == null || login.isEmpty())
          || (password == null || password.isEmpty())) {
            return;
        }
        this.name = name;
        this.login = login;
        this.password = Authenticator.passwordHash(password);
        this.reports = new HashMap<>();
        this.reportTableTitles = new HashMap<>();
    }

    private Account() {
        this.created = null;
        this.recordManager = null;
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
        return Objects.hash(created);
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

    public HashMap<String, HashMap<String, TableReport>> getReports() {
        return reports;
    }

    public HashMap<String, String> getReportTableTitles() {
        return reportTableTitles;
    }

    public void setReportTableTitle(String monthNYear, String reportTitle) {
        this.reportTableTitles.put(monthNYear, reportTitle);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

}
