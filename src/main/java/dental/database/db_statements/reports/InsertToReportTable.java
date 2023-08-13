package dental.database.db_statements.reports;

import dental.app.records.Record;
import dental.app.records.Work;
import dental.app.userset.Account;
import dental.database.db_statements.IQuery;

import java.sql.SQLException;
import java.sql.Statement;

/**
 * Executing an SQL query "INSERT" values into database's report table, created by {@link ReportTableCreator}.
 */
public class InsertToReportTable extends IQuery {


    /**
     * The title of a table to insert.
     */
    private final String table;

    final String SAMPLE_1 = "INSERT INTO %s (patient, clinic";

    final String SAMPLE_2 = ") VALUES ('%s', '%s'";

    /**
     * The {@link Record} object to insert in the report table.
     */
    final Record record;

    /**
     * Create ReportTableCreator object. Constructor executes {@linkplain IQuery#doQuery(String query)} method.
     * @param account    The {@link Account} object of the logged-in user.
     * @param record     The {@link Record} object to insert in the report table.
     * @param monthNYear The month and year to find the report table.
     * @throws SQLException If a database access error occurs or
     * this method is called on a closed {@code Statement}.
     */
    public InsertToReportTable(Account account, Record record, String monthNYear) throws SQLException {
        this.record = record;

        this.table = account.getReportTableTitles().get(monthNYear);
        if (table == null || table.isEmpty()) {
            throw new SQLException("The report table title is null or empty.");
        }

        String query = getQuery(record);
        doQuery(query);
    }

    /**
     * To build the {@linkplain Statement#execute(String) SQL query}.
     * @param record The {@link Record} object required to be saved into report table.
     * @return The string with SQL query.
     */
    private String getQuery(Record record) {

            //create string builder with the query sample
        StringBuilder resultQuery = new StringBuilder(String.format(SAMPLE_1, table));

            //check the record for the presence of works
            //if empty - to do query without work values
        if (record.getWorks().isEmpty()) {
            resultQuery.append(String.format(SAMPLE_2, record.getPatient(), record.getClinic()));
            resultQuery.append(");");
            return resultQuery.toString();
        }
        //bring the query table columns to the end by loop through the record works
        for (Work w : record.getWorks()) {
            resultQuery.append(String.format(", %s", w.title()));
        }
        //concatenate query with second sample and add a value for the patient and clinic
        resultQuery.append(String.format(SAMPLE_2, record.getPatient(), record.getClinic()));

        //bring the query work values to the end by loop
        for (Work w : record.getWorks()) {
            resultQuery.append(String.format(", %s", w.quantity()));
        }
        resultQuery.append(");");

        return resultQuery.toString();
    }

}
