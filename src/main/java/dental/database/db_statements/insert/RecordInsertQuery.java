package dental.database.db_statements.insert;

import dental.app.records.Record;
import dental.app.records.Work;
import dental.app.userset.Account;
import dental.database.DBManager;
import dental.database.db_statements.IQuery;

import java.sql.SQLException;
import java.util.ArrayList;

public class RecordInsertQuery extends IQuery {

    final Record record;

    final int accountID;
    final String patient;
    final String clinic;
    final String complete;
    final String accepted;
    final boolean closed;
    final int recordId;

    final String SAMPLE =
      "INSERT INTO records (account_id, id, patient, clinic, complete, accepted, closed) VALUES (%s, %s, '%s', '%s', '%s', '%s', %s);";

    public RecordInsertQuery(Account account, Record record) throws SQLException {
        this.record = record;
        this.accountID = account.hashCode();
        this.patient = record.getPatient();
        this.clinic = record.getClinic();
        this.complete = record.getComplete().format(DBManager.SQL_DATE_FORMAT);
        this.accepted = record.getAccepted().format(DBManager.SQL_DATE_FORMAT);
        this.closed = record.isClosed();
        this.recordId = record.hashCode();

        String query = String.format(SAMPLE,
                this.accountID, this.recordId, this.patient, this.clinic, this.complete, this.accepted, this.closed);

        doQuery(query);
        ArrayList<Work> works = record.getWorks();
        if ((works != null) && (!works.isEmpty())) {
            for (Work w : works) {
                new WorkInsertQuery(recordId, w);
            }
        }
    }
}
