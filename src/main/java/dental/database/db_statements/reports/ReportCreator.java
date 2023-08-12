package dental.database.db_statements.reports;

import dental.app.userset.Account;
import dental.database.db_statements.IQuery;

import java.sql.SQLException;
import java.time.Month;
import java.util.HashMap;

public class ReportCreator implements IQuery {

    final String SAMPLE = """
            CREATE TABLE mydb.sheet_%s (
                patient VARCHAR(20),
                clinic VARCHAR(20)""";

    private final String query;

    public ReportCreator(Account account, Month month) throws SQLException {
        this.query = getQuery(account, month);
        doQuery(query);
    }

    private String getQuery(Account account, Month month) {
            //get hashmap with the account's work types
        HashMap<String, Integer> workTypes = account.recordManager.getWorkTypes();

            //create string title for the report title in database
            // (account login and the given month)
        String reportTitle =  account.getLogin() + "_" + month.toString();

            //create string builder with the query sample
        StringBuilder resultQuery = new StringBuilder(String.format(SAMPLE, reportTitle));

            //bring the query to the end by loop
        for (String s : workTypes.keySet()) {

                //adding work types as a table columns for report sheet
            resultQuery.append(String.format(",\n\t%s INT DEFAULT 0", s));
        } resultQuery.append("\n\t);");

        return resultQuery.toString();
    }

    private String realizeMonth(String month) {

        //TODO?

        return null;
    }

    public String getQuery() {
        return query;
    }



}
