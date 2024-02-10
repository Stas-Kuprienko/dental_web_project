package edu.dental.beans;

import java.util.*;

import static edu.dental.service.WebUtility.XSSEscape;

public class ProductMap {

    private final ArrayList<Item> items;

    public ProductMap(Item[] items) {
        this.items = new ArrayList<>(List.of(items));
    }

    public boolean add(Item item) {
        return this.items.add(item);
    }

    public void update(int id, int value) {
        items.stream().filter(e -> e.id == id).findAny().ifPresent(item -> item.setValue(value));
    }

    public void remove(int id) {
        items.stream().filter(e -> e.id == id).findAny().ifPresent(items::remove);
    }

    public boolean isEmpty() {
        return items.isEmpty();
    }

    public Item[] getItems() {
        return items.toArray(new Item[]{});
    }

    @Override
    public String toString() {
        return "ProductMap{" +
                "items=" + items +
                '}';
    }

    public static class Item {

        private int id;
        private String key;
        private int value;

        public Item(int id, String key, int value) {
            this.id = id;
            this.key = key;
            this.value = value;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getKey() {
            return XSSEscape(key);
        }

        public void setKey(String key) {
            this.key = key;
        }

        public int getValue() {
            return value;
        }

        public void setValue(int value) {
            this.value = value;
        }

        @Override
        public int hashCode() {
            return Objects.hash(id, key);
        }

        @Override
            public boolean equals(Object o) {
                if (this == o) return true;
                if (o == null || getClass() != o.getClass()) return false;
                Item item = (Item) o;
                return id == item.id && value == item.value && Objects.equals(key, item.key);
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
