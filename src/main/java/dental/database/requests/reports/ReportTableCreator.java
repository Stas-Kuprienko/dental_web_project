package dental.database.requests.reports;

import dental.domain.works.RecordManager;
import dental.domain.userset.Account;
import dental.database.requests.PushRequest;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;

/**
 * Executing database queries to create report tables of monthly workRecords.
 * Create SQL table with columns - patient, clinic and work types
 *  from the account's {@linkplain RecordManager#getProductMap() work types map}.
 */
public class ReportTableCreator extends PushRequest {

    final String SAMPLE = """
            CREATE TABLE mydb.%s (
                patient VARCHAR(20),
                clinic VARCHAR(20)""";

    /**
     * Create ReportTableCreator object. Constructor executes {@linkplain PushRequest#doRequest(String query)} method.
     * @param account The {@link Account} object of the logged-in user.
     * @param month   The month for which the report is required.
     * @param year    The year of the month for which the report is required.
     * @throws SQLException If a database access error occurs or
     * this method is called on a closed {@code Statement}.
     */
    public ReportTableCreator(Account account, String month, String year) throws SQLException {
        String request = buildRequest(account, month, year);
        doRequest(request);
    }

    /**
     * To build the {@linkplain Statement#execute(String) SQL request}.
     * @param account The {@link Account} object of the logged-in user.
     * @param month   The month for which the report is required.
     * @param year    The year of the month for which the report is required.
     * @return The string with SQL request.
     */
    private String buildRequest(Account account, String month, String year) {
            //get hashmap with the account's product types
        HashMap<String, Integer> productMap = account.recordManager.getProductMap();

            //create string title for the report title in database
            // (account login and the given month)
        String reportTitle =  account.getLogin() + "_" + month + "_" + year;

            //create string builder with the request sample
        StringBuilder result = new StringBuilder(String.format(SAMPLE, reportTitle));

            //bring the request to the end by loop
        for (String s : productMap.keySet()) {

                //adding product types as a table columns for report sheet
            result.append(String.format(",\n\t%s INT DEFAULT 0", s));
        } result.append("\n\t);");

        account.setReportTableTitle(month + "_" + year, reportTitle);
        return result.toString();
    }

    private String realizeMonth(String month) {

        //TODO?

        return null;
    }

}
