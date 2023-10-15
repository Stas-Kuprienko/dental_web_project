package edu.dental.domain.records.my_work_record_book;

import edu.dental.domain.entities.Product;
import edu.dental.domain.records.ProductMap;

import java.util.*;

public class MyProductMap implements ProductMap {

    private static final byte DEFAULT_CAPACITY = 10;

    private int size;

    protected Item[] items;


    public MyProductMap() {
        items = new Item[DEFAULT_CAPACITY];
        size = 0;
    }

    public MyProductMap(Item[] entries) {
        size = entries.length;
        this.items = entries;
    }

    @Override
    public Product createProduct(String title, int quantity) {
        int i = findIndex(title);
        if (i == -1) {
            throw new NoSuchElementException("type title is not found");
        } else {
            Item node = items[i];
            return new Product(node.getKey(), (byte) quantity, node.getValue());
        }
    }

    @Override
    public String getTitleByID(int id) {
        for (Item i : items) {
            if (i.id == id) {
                return i.title;
            }
        }
        return null;
    }

    @Override
    public Integer put(String key, Integer value) {
        if (key == null) {
            throw new NullPointerException("the given key is empty");
        }
        if (size == items.length) {
            grow();
        }
        int i = findIndex(key);
        if (i > -1) {
            int previous = items[i].getValue();
            items[i].setValue(value);
            return previous;
        } else {
            items[size] = new Item(key.toLowerCase(), value);
            size++;
            return null;
        }
    }

    @Override
    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public boolean containsKey(Object key) {
        return findIndex((String) key) > -1;
    }

    @Override
    public boolean containsValue(Object value) {
        int v = (int) value;
        for (Item e : items) {
            if (e.getValue() == v) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Integer get(Object key) {
        String strKey = (String) key;
        int i = findIndex(strKey.toLowerCase());
        if (i == -1) {
            throw new NullPointerException("the specified value is not found");
        } else {
            return items[i].getValue();
        }
    }

    @Override
    public Integer remove(Object key) {
        String strKey = (String) key;
        int i = findIndex(strKey.toLowerCase());
        if (i == -1) {
            return null;
        } else {
            size -= 1;
            int value = items[i].getValue();
            items[i] = null;
            if (size > i) {
                System.arraycopy(items, i + 1, items, i, size - i);
            }
            return value;
        }
    }

    @Override
    public void putAll(java.util.Map<? extends String, ? extends Integer> m) {
        if (items == null) {
            items = new Item[m.size()];
        }
        for (java.util.Map.Entry<? extends String, ? extends Integer> entry : m.entrySet()) {
            put(entry.getKey(), entry.getValue());
        }
    }

    @Override
    public void clear() {
        if (size != 0) {
            items = new Item[items.length];
            size = 0;
        }
    }

    @Override
    public Set<String> keySet() {
        return Set.of(keysToArray());
    }

    @Override
    public Collection<Integer> values() {
        if (size == 0) {
            return List.of();
        }
        Integer[] prices = new Integer[size];
        for (int i = 0; i < size; i++) {
            prices[i] = items[i].getValue();
        }
        return Arrays.asList(prices);
    }

    @Override
    public Set<java.util.Map.Entry<String, Integer>> entrySet() {
        return Set.of(toArray());
    }

    public String[] keysToArray() {
        String[] result = new String[size];
        for (int i = 0; i < size; i++) {
            result[i] = items[i].getKey();
        }
        return result;
    }

    public Item[] toArray() {
        if (size == 0) {
            throw new NullPointerException("The array of entries is empty.");
        }
        Item[] arr = new Item[size];
        System.arraycopy(items, 0, arr, 0, size);
        return arr;
    }

    private int findIndex(String key) {
        if (key == null) {
            throw new NullPointerException("the given argument is null");
        }
        if (size < 1) {
            return -1;
        }
        key = key.toLowerCase();
        for (int i = 0; i < size; i++) {
            if (items[i].getKey().equals(key)) {
                return i;
            }
        }
        return -1;
    }

    private void grow() {
        Item[] result = new Item[size << 1];
        System.arraycopy(items, 0, result, 0, items.length);
        this.items = result;
    }


    public static class Item implements ProductMap.Item {

        private int id;
        private final String title;
        private int price;

        Item(String title, int price) {
            this.title = title;
            this.price = price;
        }

        @Override
        public String getKey() {
            return title;
        }

        @Override
        public Integer getValue() {
            return price;
        }

        @Override
        public Integer setValue(Integer value) {
            int previous = price;
            this.price = value;
            return previous;
        }

        @Override
        public int getId() {
            return id;
        }

        @Override
        public void setId(int id) {
            this.id = id;
        }
    }
}
