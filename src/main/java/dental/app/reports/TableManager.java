package dental.app.reports;

import dental.app.userset.Account;

/**
 * The ReportManager class is used to manage {@link TableReport report} objects.
 *  All methods of the class are static, so the class does not need to implement instances.
 */
public class TableManager {

    private TableManager() {
    }

    /**
     * Create the file(.txt) with monthly report table.
     * @param account The {@link Account} object that requires.
     * @param report The {@link TableReport} object which needs to convert to a file.
     */
    public static void createFileReport(Account account, TableReport report) {

    }

    /**
     * To search a {@link TableReport report} object for a given month.
     * @param account The {@link Account} object that requires.
     * @param month The month for which the report is required.
     * @return The {@link TableReport} object.
     */
    public static TableReport searchReport(Account account, String month) {

        return null;
    }

}
