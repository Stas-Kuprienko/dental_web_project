package dental.domain.works;

import dental.domain.data_structures.MyList;

import java.io.Serializable;
import java.util.Objects;

/**
 * The class represent the product object and contains entries title, price and quantity of product items.
 */
public record Product(String title, byte quantity, int price) implements Serializable {

    /**
     * Count total amount of the product price.
     * @return The value of the amount
     */
    public int countAmount() {
        return quantity * price;
    }

    /**
     * Methods that using for {@linkplain MyList#searchByString(MyList.Searchable, String, Class) searching} objects.
     */
    public enum SearchBy implements MyList.Searchable {
        title
    }

        @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return quantity == product.quantity && price == product.price && Objects.equals(title, product.title);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, quantity, price);
    }

    @Override
    public String toString() {
        return "Product{" +
                "title='" + title + '\'' +
                ", quantity=" + quantity +
                ", price=" + countAmount() +
                '}';
    }
}
