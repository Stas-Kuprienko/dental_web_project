package dental.database.db_statements;

import dental.app.records.Record;
import dental.database.DBManager;

import java.sql.SQLException;

public class RecordInsertOrder implements DBQuery {

    final Record record;

    final int accountID;
    final String patient;
    final String clinic;
    final String complete;
    final String accepted;
    final boolean closed;
    final int recordId;

    final String SAMPLE = "INSERT INTO records (account_id, id, patient, clinic, complete, accepted, closed)" +
            "VALUES (%s, %s, %s, %s, %s, %s, %s);";
    private final String query;

    public RecordInsertOrder(int accountID, Record record) {
        this.record = record;
        this.accountID = accountID;
        this.patient = record.getPatient();
        this.clinic = record.getClinic();
        this.complete = record.getComplete().format(DBManager.SQL_DATE_FORMAT);
        this.accepted = record.getAccepted().format(DBManager.SQL_DATE_FORMAT);
        this.closed = record.isClosed();
        this.recordId = record.hashCode();

        query = String.format(SAMPLE,
                this.accountID, this.recordId, this.patient, this.clinic, this.complete, this.accepted, this.closed);
    }

    //TODO WorkObjectsOrder

    @Override
    public boolean doQuery() {
        try {
            return DBQuery.state != null && DBQuery.state.execute(query);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
