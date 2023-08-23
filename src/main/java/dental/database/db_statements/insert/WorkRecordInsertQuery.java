package dental.database.db_statements.insert;

import dental.app.MyList;
import dental.app.works.Product;
import dental.app.works.WorkRecord;
import dental.app.userset.Account;
import dental.database.DBManager;
import dental.database.db_statements.PushQuery;

import java.sql.SQLException;

public class WorkRecordInsertQuery extends PushQuery {

    final WorkRecord workRecord;

    final int accountID;
    final String patient;
    final String clinic;
    final String complete;
    final String accepted;
    final boolean closed;
    final int workId;

    final String SAMPLE =
      "INSERT INTO work_records (account_id, id, patient, clinic, complete, accepted, closed) VALUES (%s, %s, '%s', '%s', '%s', '%s', %s);";

    public WorkRecordInsertQuery(Account account, WorkRecord workRecord) throws SQLException {
        this.workRecord = workRecord;
        this.accountID = account.hashCode();
        this.patient = workRecord.getPatient();
        this.clinic = workRecord.getClinic();
        this.complete = workRecord.getComplete().format(DBManager.SQL_DATE_FORMAT);
        this.accepted = workRecord.getAccepted().format(DBManager.SQL_DATE_FORMAT);
        this.closed = workRecord.getClosed();
        this.workId = workRecord.hashCode();

        String query = String.format(SAMPLE,
                this.accountID, this.workId, this.patient, this.clinic, this.complete, this.accepted, this.closed);

        doQuery(query);
        MyList<Product> products = workRecord.getProducts();
        if ((products != null) && (!products.isEmpty())) {
            for (Product p : products) {
                new ProductInsertQuery(workId, p);
            }
        }
    }
}
