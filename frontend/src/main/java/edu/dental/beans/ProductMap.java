package edu.dental.beans;

import java.util.*;

public class ProductMap {

    private ArrayList<Item> items;

    public ProductMap(Item[] items) {
        this.items = new ArrayList<>(List.of(items));
    }
    public ProductMap() {
        this.items = new ArrayList<>();
    }


    public String[] getKeys() {
        String[] result = new String[items.size()];
        return items.stream().map(e -> e.key).toList().toArray(result);
    }

    public boolean add(Item item) {
        return this.items.add(item);
    }

    public void update(String key, int value) {
        Item item = items.stream().filter(e -> e.key.equals(key)).findAny().orElse(null);
        if (item != null) {
            int id = item.id;
            items.remove(item);
            items.add(new Item(id, key, value));
        }
    }

    public void remove(String key) {
        items.stream().filter(e -> e.key.equals(key)).findAny().ifPresent(items::remove);
    }

    public boolean isEmpty() {
        return items.isEmpty();
    }

    public ArrayList<Item> getItems() {
        return items;
    }

    public void setItems(ArrayList<Item> items) {
        this.items = items;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProductMap that = (ProductMap) o;
        return Objects.equals(items, that.items);
    }

    @Override
    public int hashCode() {
        return Objects.hash(items);
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

        public Item(int id, String key, int value) {}

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getKey() {
            return key;
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
