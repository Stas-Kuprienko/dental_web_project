package dental.database.requests.insert;

import dental.app.works.Product;
import dental.database.requests.PushRequest;

import java.sql.SQLException;

public class ProductInsertRequest extends PushRequest {

    final Product product;

    final int workID;
    final String title;
    final byte quantity;
    final int price;

    final String SAMPLE = "INSERT INTO products (work_id, title, quantity, price) VALUES (%s, '%s', %s, %s);";

    public ProductInsertRequest(int workID, Product product) throws SQLException {
        this.product = product;
        this.workID = workID;
        this.title = product.title();
        this.quantity = product.quantity();
        this.price = product.price();

        String request = String.format(SAMPLE, this.workID, this.title, this.quantity, this.price);

        doRequest(request);
    }

}
