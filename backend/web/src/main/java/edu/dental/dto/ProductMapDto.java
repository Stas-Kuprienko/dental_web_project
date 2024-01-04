package edu.dental.dto;

import java.util.Arrays;
import java.util.Iterator;
import java.util.Objects;

public class ProductMapDto {

    private Item[] items;

    public ProductMapDto(Item[] items) {
        this.items = items;
    }

    public ProductMapDto(edu.dental.entities.ProductMap map) {
        this.items = new Item[map.size()];
        edu.dental.entities.ProductMap.Item[] items = map.toArray();
        for (int i = 0; i < map.size(); i++) {
            this.items[i] = new Item(items[i]);
        }
    }

    public Iterator<Item> iterator() {
        return Arrays.stream(items).iterator();
    }

    public String[] getKeys() {
        String[] result = new String[items.length];
        return Arrays.stream(items).map(e -> e.key).toList().toArray(result);
    }

    public Item[] getItems() {
        return items;
    }

    public void setItems(Item[] items) {
        this.items = items;
    }

    public boolean isEmpty() {
        return items.length == 0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProductMapDto that = (ProductMapDto) o;
        return Arrays.equals(items, that.items);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(items);
    }

    @Override
    public String toString() {
        return "ProductMapDto{" +
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


        public Item(edu.dental.entities.ProductMap.Item item) {
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
