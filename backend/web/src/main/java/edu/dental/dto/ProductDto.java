package edu.dental.dto;

import edu.dental.entities.Product;

import java.io.Serializable;
import java.util.Objects;

/**
 * The class represent the product object and contains entries id, title, price and quantity of product items.
 */
public class ProductDto implements Serializable {

    private int entryId;
    private String title;
    private byte quantity;
    private int price;

    public ProductDto() {}

    public ProductDto(int entryId, String title, byte quantity, int price) {
        this.entryId = entryId;
        this.title = title;
        this.quantity = quantity;
        this.price = price;
    }

    public ProductDto(Product product) {
        this.entryId = product.entryId();
        this.title = product.title();
        this.quantity = product.quantity();
        this.price = product.price();
    }

    public int getEntryId() {
        return entryId;
    }

    public void setEntryId(int entryId) {
        this.entryId = entryId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public byte getQuantity() {
        return quantity;
    }

    public void setQuantity(byte quantity) {
        this.quantity = quantity;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProductDto product = (ProductDto) o;
        return quantity == product.quantity && price == product.price && Objects.equals(title, product.title);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, quantity, price);
    }

    @Override
    public String toString() {
        return "\n ProductDto{" +
                "\n -title='" + title + '\'' +
                ", \n -quantity=" + quantity +
                ", \n -price=" + price +
                '}';
    }
}
