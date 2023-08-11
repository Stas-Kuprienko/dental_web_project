package dental.database.db_statements;

import dental.app.records.Work;

import java.sql.SQLException;

public class WorkInsertQuery implements IQuery {

    final Work work;

    final int recordID;
    final String title;
    final byte quantity;
    final int price;

    final String SAMPLE = "INSERT INTO work_position (record_id, title, quantity, price) VALUES (%s, %s, %s, %s);";

    public WorkInsertQuery(int recordID, Work work) throws SQLException {
        this.work = work;
        this.recordID = recordID;
        this.title = work.title();
        this.quantity = work.quantity();
        this.price = work.price();

        String query = String.format(SAMPLE, this.recordID, this.title, this.quantity, this.price);

        if (!doQuery(query)) {
            throw new SQLException();
        }
    }

}
