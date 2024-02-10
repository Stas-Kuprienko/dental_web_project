package edu.dental.beans;

import java.io.Serializable;
import java.util.Objects;

import static edu.dental.service.WebUtility.XSSEscape;

/**
 * The class represent the product object and contains entries id, title, price and quantity of product items.
 */
public class Product implements Serializable {

    private int id;
    private String title;
    private byte quantity;
    private int price;

    public Product(int id, String title, byte quantity, int price) {
        this.id = id;
        this.title = title;
        this.quantity = quantity;
        this.price = price;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return XSSEscape(title);
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
        Product product = (Product) o;
        return quantity == product.quantity && price == product.price && Objects.equals(title, product.title);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, quantity, price);
    }

    @Override
    public String toString() {
        return "\n Product{" +
                "\n -title='" + title + '\'' +
                ", \n -quantity=" + quantity +
                ", \n -price=" + price +
                '}';
    }
}
