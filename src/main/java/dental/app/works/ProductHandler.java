package dental.app.works;

import java.util.HashMap;

public class ProductHandler {

    ProductHandler() {
        this.productMap = new HashMap<>();
    }

    /**
     * The {@link HashMap map} of the work types and prices.
     */
    private final HashMap<String, Integer> productMap;


    /**
     * Create a new {@link Product} object for entry a record.
     * @param title  The title of the work type.
     * @param quantity The quantity of the work items.
     * @return The {@link Product} object.
     */
    protected Product createProduct(String title, int quantity) {
        if (((title == null) || title.isEmpty())) {
            return null;
        } else {
            title = title.toLowerCase();
            return new Product(title,(byte) quantity, productMap.get(title));
        }
    }

    /**
     * Enter the type of work in the HashMap.
     * @param title The title of the work type.
     * @param price The price of the work type.
     */
    public void addProductInMap(String title, int price) {
        if ((title == null || title.isEmpty()) || (price < 1)) {
            return;
        }
        title = title.toLowerCase();
        productMap.put(title, price);
    }

    /**
     * Remove the type of product from the {@linkplain ProductHandler#productMap}
     *  by a {@linkplain java.util.HashMap#get(Object) key}.
     * @param title The title of the product type as a Key
     * @return True if the Key of the product type removed
     * or false if no such element
     */
    public boolean removeFromProductMap(String title) {
        return productMap.remove(title.toLowerCase()) != null;
    }


    /**
     * Find the {@link Product} object in {@link WorkRecord} by title.
     * @param title The title String of the required work position.
     * @return The required {@link Product} object, if such exists, or null.
     */
    protected Product findProduct(WorkRecord workRecord, String title) {
        for (Product p : workRecord.getProducts()) {
            if (p.title().equalsIgnoreCase(title)) {
                return p;
            }
        }
        return null;
    }



    public HashMap<String, Integer> getProductMap() {
        return productMap;
    }
}
