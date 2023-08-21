package dental.database.db_statements.reports;

import dental.app.works.RecordManager;
import dental.app.userset.Account;
import dental.database.db_statements.IQuery;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;

/**
 * Executing database queries to create report tables of monthly workRecords.
 * Create SQL table with columns - patient, clinic and work types
 *  from the account's {@linkplain RecordManager#getProductMap() work types map}.
 */
public class ReportTableCreator extends IQuery {

    final String SAMPLE = """
            CREATE TABLE mydb.%s (
                patient VARCHAR(20),
                clinic VARCHAR(20)""";

    /**
     * Create ReportTableCreator object. Constructor executes {@linkplain IQuery#doQuery(String query)} method.
     * @param account The {@link Account} object of the logged-in user.
     * @param month   The month for which the report is required.
     * @param year    The year of the month for which the report is required.
     * @throws SQLException If a database access error occurs or
     * this method is called on a closed {@code Statement}.
     */
    public ReportTableCreator(Account account, String month, String year) throws SQLException {
        String query = buildQuery(account, month, year);
        doQuery(query);
    }

    /**
     * To build the {@linkplain Statement#execute(String) SQL query}.
     * @param account The {@link Account} object of the logged-in user.
     * @param month   The month for which the report is required.
     * @param year    The year of the month for which the report is required.
     * @return The string with SQL query.
     */
    private String buildQuery(Account account, String month, String year) {
            //get hashmap with the account's product types
        HashMap<String, Integer> productMap = account.recordManager.getProductMap();

            //create string title for the report title in database
            // (account login and the given month)
        String reportTitle =  account.getLogin() + "_" + month + "_" + year;

            //create string builder with the query sample
        StringBuilder resultQuery = new StringBuilder(String.format(SAMPLE, reportTitle));

            //bring the query to the end by loop
        for (String s : productMap.keySet()) {

                //adding product types as a table columns for report sheet
            resultQuery.append(String.format(",\n\t%s INT DEFAULT 0", s));
        } resultQuery.append("\n\t);");

        account.setReportTableTitle(month + "_" + year, reportTitle);
        return resultQuery.toString();
    }

    private String realizeMonth(String month) {

        //TODO?

        return null;
    }

}
