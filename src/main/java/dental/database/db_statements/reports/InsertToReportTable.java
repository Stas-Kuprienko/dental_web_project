package dental.database.db_statements.reports;

import dental.app.records.Record;
import dental.app.records.Work;
import dental.app.userset.Account;
import dental.database.db_statements.IQuery;

import java.sql.SQLException;

public class InsertToReportTable extends IQuery {

    private final String table;

    final String SAMPLE_1 = "INSERT INTO %s (patient, clinic";

    final String SAMPLE_2 = ") VALUES ('%s', '%s'";

    final Record record;

    public InsertToReportTable(Account account, Record record) throws SQLException {
        this.record = record;

        this.table = account.getReportTableTitle();
        if (table == null || table.isEmpty()) {
            throw new SQLException("The report table title is null or empty.");
        }

        String query = getQuery(record);
        doQuery(query);
    }

    private String getQuery(Record record) {
        StringBuilder resultQuery = new StringBuilder(String.format(SAMPLE_1, table));

        if (record.getWorks().isEmpty()) {
            return null;
        }
        for (Work w : record.getWorks()) {
            resultQuery.append(String.format(", %s", w.title()));
        }
        resultQuery.append(String.format(SAMPLE_2, record.getPatient(), record.getClinic()));
        for (Work w : record.getWorks()) {
            resultQuery.append(String.format(", %s", w.quantity()));
        }
        resultQuery.append(");");

        return resultQuery.toString();
    }

}
