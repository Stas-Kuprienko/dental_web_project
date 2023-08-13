package dental.database.db_statements.reports;

import dental.app.records.Record;
import dental.app.userset.Account;
import dental.database.db_statements.IQuery;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.HashMap;

public class ReportTableCreator extends IQuery {

    final String SAMPLE = """
            CREATE TABLE mydb.%s (
                patient VARCHAR(20),
                clinic VARCHAR(20)""";

    public ReportTableCreator(Account account, String month, String year) throws SQLException {
        String query = getQuery(account, month, year);
        doQuery(query);
    }

    private String getQuery(Account account, String month, String year) {
            //get hashmap with the account's work types
        HashMap<String, Integer> workTypes = account.recordManager.getWorkTypes();

            //create string title for the report title in database
            // (account login and the given month)
        String reportTitle =  account.getLogin() + "_" + month + "_" + year;

            //create string builder with the query sample
        StringBuilder resultQuery = new StringBuilder(String.format(SAMPLE, reportTitle));

            //bring the query to the end by loop
        for (String s : workTypes.keySet()) {

                //adding work types as a table columns for report sheet
            resultQuery.append(String.format(",\n\t%s INT DEFAULT 0", s));
        } resultQuery.append("\n\t);");

        account.setReportTableTitle(reportTitle);
        return resultQuery.toString();
    }

    private String realizeMonth(String month) {

        //TODO?

        return null;
    }

}
