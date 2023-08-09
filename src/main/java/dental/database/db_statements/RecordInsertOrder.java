package dental.database.db_statements;

import dental.app.records.Record;

import java.sql.SQLException;
import java.time.LocalDate;

public class RecordInsertOrder implements DBQuery {

    final Record record;

    final int accountID;
    final String patient;
    final String clinic;
    final LocalDate complete;
    final LocalDate accepted;
    final boolean closed;
    final int recordId;

    //TODO INSERT by String.format()
    private final String query;

    public RecordInsertOrder(int accountID, Record record) {
        this.record = record;
        this.accountID = accountID;
        this.patient = record.getPatient();
        this.clinic = record.getClinic();
        this.complete = record.getComplete();
        this.accepted = record.getAccepted();
        this.closed = record.isClosed();
        this.recordId = record.hashCode();

        query = "";
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
