package dental.database.requests.insert;

import dental.app.MyList;
import dental.app.works.Product;
import dental.app.works.WorkRecord;
import dental.app.userset.Account;
import dental.database.DBManager;
import dental.database.requests.PushRequest;

import java.sql.SQLException;

public class WorkRecordInsertRequest extends PushRequest {

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

    public WorkRecordInsertRequest(Account account, WorkRecord workRecord) throws SQLException {
        this.workRecord = workRecord;
        this.accountID = account.hashCode();
        this.patient = workRecord.getPatient();
        this.clinic = workRecord.getClinic();
        this.complete = workRecord.getComplete().format(DBManager.SQL_DATE_FORMAT);
        this.accepted = workRecord.getAccepted().format(DBManager.SQL_DATE_FORMAT);
        this.closed = workRecord.isClosed();
        this.workId = workRecord.hashCode();

        String request = String.format(SAMPLE,
                this.accountID, this.workId, this.patient, this.clinic, this.complete, this.accepted, this.closed);

        doRequest(request);
        MyList<Product> products = workRecord.getProducts();
        if ((products != null) && (!products.isEmpty())) {
            for (Product p : products) {
                new ProductInsertRequest(workId, p);
            }
        }
    }
}
