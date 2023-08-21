package dental.database.db_statements.insert;

import dental.app.works.Product;
import dental.database.db_statements.IQuery;

import java.sql.SQLException;

public class ProductInsertQuery extends IQuery {

    final Product product;

    final int workID;
    final String title;
    final byte quantity;
    final int price;

    final String SAMPLE = "INSERT INTO products (work_id, title, quantity, price) VALUES (%s, '%s', %s, %s);";

    public ProductInsertQuery(int workID, Product product) throws SQLException {
        this.product = product;
        this.workID = workID;
        this.title = product.title();
        this.quantity = product.quantity();
        this.price = product.price();

        String query = String.format(SAMPLE, this.workID, this.title, this.quantity, this.price);

        doQuery(query);
    }

}
