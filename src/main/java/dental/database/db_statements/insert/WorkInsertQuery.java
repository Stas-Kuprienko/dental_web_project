package dental.database.db_statements.insert;

import dental.app.records.Work;
import dental.database.db_statements.IQuery;

import java.sql.SQLException;

public class WorkInsertQuery extends IQuery {

    final Work work;

    final int recordID;
    final String title;
    final byte quantity;
    final int price;

    final String SAMPLE = "INSERT INTO work_position (record_id, title, quantity, price) VALUES (%s, '%s', %s, %s);";

    public WorkInsertQuery(int recordID, Work work) throws SQLException {
        this.work = work;
        this.recordID = recordID;
        this.title = work.title();
        this.quantity = work.quantity();
        this.price = work.price();

        String query = String.format(SAMPLE, this.recordID, this.title, this.quantity, this.price);

        doQuery(query);
    }

}
