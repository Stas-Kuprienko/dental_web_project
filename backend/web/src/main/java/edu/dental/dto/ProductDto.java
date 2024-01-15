package edu.dental.dto;

import edu.dental.entities.Product;

import java.io.Serializable;
import java.util.Objects;

/**
 * The class represent the product object and contains entries id, title, price and quantity of product items.
 */
public record ProductDto(int entryId, String title, byte quantity, int price) implements Serializable {

    public static ProductDto parse(Product product) {
        return new ProductDto(product.entryId(), product.title(), product.quantity(), product.price());
    }

    public static Product[] revert(ProductDto[] dtoArray) {
        Product[] result = new Product[dtoArray.length];
        for (int i = 0; i < dtoArray.length; i++) {
            ProductDto dto = dtoArray[i];
            result[i] = new Product(dto.entryId, dto.title, dto.quantity, dto.price);
        }
        return result;
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
