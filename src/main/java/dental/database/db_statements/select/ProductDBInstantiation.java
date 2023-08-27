package dental.database.db_statements.select;

import dental.app.works.Product;

import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;

public class ProductDBInstantiation extends ObjectDBPrototype<Product> {

    private static final String SELECTABLE = "*";

    private static final String FROM = "products";

    private static final String TITLE = "title";
    private static final String QUANTITY = "quantity";
    private static final String PRICE = "price";


    public ProductDBInstantiation(String whereField, String whereValue) throws SQLException {
        super(SELECTABLE, FROM, whereField, whereValue);
    }

    public ProductDBInstantiation() throws SQLException {
        super(SELECTABLE, FROM);
    }


    @Override
    public ProductDBInstantiation build() throws SQLException, IllegalAccessException {

        while (result.next()) {
            String title = result.getString(TITLE);
            byte quantity = result.getByte(QUANTITY);
            int price = result.getInt(PRICE);
            try {
                Product product = Product.class.getConstructor(String.class, byte.class, int.class).newInstance(title, quantity, price);
                list.add(product);
            } catch (NoSuchMethodException | InvocationTargetException | InstantiationException e) {
                e.printStackTrace();
            }
        }
        return this;
    }
}
