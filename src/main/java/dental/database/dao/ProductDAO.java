package dental.database.dao;

import dental.app.MyList;
import dental.app.works.Product;

import java.sql.SQLException;

public class ProductDAO implements DAO<Product> {

    private ProductDAO() {}
    static {
        instance = new ProductDAO();
    }

    private static final ProductDAO instance;

    @Override
    public void add(Product product) throws SQLException {

    }

    public void addAll(MyList<Product> products) {

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

    public static ProductDAO getInstance() {
        return instance;
    }

}
