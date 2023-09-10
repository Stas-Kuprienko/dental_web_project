package dental.database.dao;

import dental.app.MyList;
import dental.app.works.Product;
import dental.database.DBConfig;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ProductDAO implements DAO<Product> {

    protected ProductDAO(int workRecordId) {
        this.workRecordId = workRecordId;
    }

    public static final String TABLE_NAME = "product";

    private final int workRecordId;

    @Override
    public void add(Product product) throws SQLException {
        if (workRecordId < 1) {
            throw new SQLException("work_id is incorrect");
        }
        String query = String.format("INSERT INTO %s.%s (work_id, title, quantity, price) VALUES (?, ?, ?, ?);", DBConfig.DATA_BASE, TABLE_NAME);
        DBRequest request = new DBRequest(query);
        PreparedStatement statement = request.getStatement();
        statement.setInt(1, workRecordId);
        statement.setString(2, product.title());
        statement.setByte(3, product.quantity());
        statement.setInt(4, product.price());
        statement.execute();
        request.close();
    }

    @Override
    public MyList<Product> getAll() throws Exception {
        return null;
    }

    @Override
    public Product get(int id) throws Exception {
        return null;
    }

    @Override
    public void remove(int id) throws SQLException {

    }

    @Override
    public void remove(Product product) throws SQLException {

    }

    public int getWorkRecordId() {
        return workRecordId;
    }
}
