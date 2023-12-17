package edu.dental.dto;

import java.util.Arrays;
import java.util.Iterator;
import java.util.Objects;

public class ProductMap {

    private Item[] items;

    public ProductMap(Item[] items) {
        this.items = items;
    }

    public ProductMap(edu.dental.domain.records.ProductMap map) {
        this.items = new Item[map.size()];
        edu.dental.domain.records.ProductMap.Item[] items1 = map.toArray();
        for (int i = 0; i < map.size(); i++) {
            items[i] = new Item(items1[i]);
        }
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

    public static class Item {

        private int id;
        private String key;
        private int value;

        public Item(int id, String key, int value) {
            this.id = id;
            this.key = key;
            this.value = value;
        }


        public Item(edu.dental.domain.records.ProductMap.Item item) {
            this.id = item.getId();
            this.key = item.getKey();
            this.value = item.getValue();
        }

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
            Item itemDTO = (Item) o;
            return id == itemDTO.id && value == itemDTO.value && Objects.equals(key, itemDTO.key);
        }

        @Override
        public int hashCode() {
            return Objects.hash(id, key, value);
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
