package edu.dental.domain.entities;

import java.io.Serializable;
import java.util.Objects;

/**
 * The class represent the product object and contains entries id, title, price and quantity of product items.
 */
public record Product (int entryId, String title, byte quantity, int price) implements Serializable {

    /**
     * Count total amount of the product price.
     * @return The value of the amount
     */
    public int countAmount() {
        return quantity * price;
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
