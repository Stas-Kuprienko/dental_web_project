package edu.dental.beans;

import java.util.Arrays;
import java.util.Iterator;
import java.util.Objects;

public class ProductMap {

    private Item[] items;

    public ProductMap(Item[] items) {
        this.items = items;
    }

    public Iterator<Item> iterator() {
        return Arrays.stream(items).iterator();
    }

    public String[] getKeys() {
        String[] result = new String[items.length];
        return Arrays.stream(items).map(e -> e.key).toList().toArray(result);
    }

    public boolean isEmpty() {
        return items.length == 0;
    }

    public Item[] getItems() {
        return items;
    }

    public void setItems(Item[] items) {
        this.items = items;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProductMap that = (ProductMap) o;
        return Arrays.equals(items, that.items);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(items);
    }

    @Override
    public String toString() {
        return "ProductMap{" +
                "items=" + Arrays.toString(items) +
                '}';
    }

    public record Item(int id, String key, int value) {

        @Override
            public boolean equals(Object o) {
                if (this == o) return true;
                if (o == null || getClass() != o.getClass()) return false;
                Item itemDTO = (Item) o;
                return id == itemDTO.id && value == itemDTO.value && Objects.equals(key, itemDTO.key);
            }

        @Override
            public String toString() {
                return "Item{" +
                        "id=" + id +
                        ", key='" + key + '\'' +
                        ", value=" + value +
                        '}';
            }
        }
}
