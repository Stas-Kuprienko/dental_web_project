package dental.database.requests.reports;

import dental.app.works.WorkRecord;
import dental.app.works.Product;
import dental.app.userset.Account;
import dental.database.requests.PushRequest;

import java.sql.SQLException;
import java.sql.Statement;

/**
 * Executing an SQL query "INSERT" values into database's report table, created by {@link ReportTableCreator}.
 */
public class ReportTableInsertRequest extends PushRequest {


    /**
     * The title of a table to insert.
     */
    private final String table;

    final String SAMPLE_1 = "INSERT INTO %s (patient, clinic";

    final String SAMPLE_2 = ") VALUES ('%s', '%s'";

    /**
     * The {@link WorkRecord} object to insert in the report table.
     */
    final WorkRecord workRecord;

    /**
     * Create ReportTableCreator object. Constructor executes {@linkplain PushRequest#doRequest(String query)} method.
     * @param account    The {@link Account} object of the logged-in user.
     * @param workRecord     The {@link WorkRecord} object to insert in the report table.
     * @param monthNYear The month and year to find the report table.
     * @throws SQLException If a database access error occurs or
     * this method is called on a closed {@code Statement}.
     */
    public ReportTableInsertRequest(Account account, WorkRecord workRecord, String monthNYear) throws SQLException {
        this.workRecord = workRecord;

        this.table = account.getReportTableTitles().get(monthNYear);
        if (table == null || table.isEmpty()) {
            throw new SQLException("The report table title is null or empty.");
        }

        String query = buildQuery(workRecord);
        doRequest(query);
    }

    /**
     * To build the {@linkplain Statement#execute(String) SQL query}.
     * @param workRecord The {@link WorkRecord} object required to be saved into report table.
     * @return The string with SQL query.
     */
    private String buildQuery(WorkRecord workRecord) {

            //create string builder with the query sample
        StringBuilder resultQuery = new StringBuilder(String.format(SAMPLE_1, table));

            //check the workRecord for the presence of products
            //if empty - to do query without product values
        if (workRecord.getProducts().isEmpty()) {
            resultQuery.append(String.format(SAMPLE_2, workRecord.getPatient(), workRecord.getClinic()));
            resultQuery.append(");");
            return resultQuery.toString();
        }
        //bring the query table columns to the end by loop through the work products
        for (Product p : workRecord.getProducts()) {
            resultQuery.append(String.format(", %s", p.title()));
        }
        //concatenate query with second sample and add a value for the patient and clinic
        resultQuery.append(String.format(SAMPLE_2, workRecord.getPatient(), workRecord.getClinic()));

        //bring the query product values to the end by loop
        for (Product p : workRecord.getProducts()) {
            resultQuery.append(String.format(", %s", p.quantity()));
        }
        resultQuery.append(");");

        return resultQuery.toString();
    }

}
