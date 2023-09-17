package dental.database.dao;

import dental.domain.MyList;
import dental.domain.works.Product;
import dental.database.DBConfig;

import java.lang.reflect.Constructor;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ProductDAO implements DAO<Product> {

    protected ProductDAO(int accountId, int workRecordId) {
        this.accountId = accountId;
        this.workRecordId = workRecordId;
    }

    //TODO
    //TODO
    //TODO

    public static final String TABLE_NAME = DBConfig.DATA_BASE + ".product";

    private static final String PRODUCT_FIELDS = "title, quantity, price";

    private final int accountId;
    private final int workRecordId;

    @Override
    public boolean insert(Product product) throws SQLException {
        String IDs = "account_id, work_id, ";
        String query = String.format(
                SQL_DAO.INSERT.QUERY, TABLE_NAME, IDs + PRODUCT_FIELDS, "?".repeat(PRODUCT_FIELDS.split(",").length) + 1);
        DBRequest request = new DBRequest(query);
        PreparedStatement statement = request.getStatement();
        statement.setInt(1, workRecordId);
        statement.setString(2, product.title());
        statement.setByte(3, product.quantity());
        statement.setInt(4, product.price());
        boolean isSuccess = statement.execute();
        request.close();
        return isSuccess;
    }

    @Override
    public MyList<Product> getAll() throws Exception {
        String where = "account_id = ? AND work_id = ?";
        String query = String.format(SQL_DAO.SELECT_WHERE.QUERY, PRODUCT_FIELDS, TABLE_NAME, where);
        DBRequest request = new DBRequest(query);
        request.getStatement().setInt(1, accountId);
        request.getStatement().setInt(2, workRecordId);
        ResultSet resultSet = request.getStatement().executeQuery();
        MyList<Product> products = new ProductInstantiation(resultSet).products;
        request.close();
        return products;
    }

    @Override
    public Product get(int id) throws Exception {
        return null;
    }

    @Override
    public boolean remove(int id) throws SQLException {
        return true;
    }

    @SuppressWarnings("all")
    private class ProductInstantiation extends Instantiation<Product> {

        private final ResultSet resultSet;
        private final MyList<Product> products;
        private final Constructor<Product> constructor;

        private ProductInstantiation(ResultSet resultSet) throws Exception {
            this.resultSet = resultSet;
            products = new MyList<>();
            constructor = Product.class.getConstructor(String.class, byte.class, int.class);
            constructor.setAccessible(true);
            build();
            constructor.setAccessible(true);
            resultSet.close();
        }

        @Override
        protected void build() throws Exception {
            while(resultSet.next()) {
                String title = resultSet.getString("title");
                byte quantity = resultSet.getByte("quantity");
                int price = resultSet.getInt("price");
                Product product = constructor.newInstance(title, quantity, price);
                products.add(product);
            }
        }
    }
}
