package edu.dental.domain.records.my_work_record_book;

import edu.dental.domain.entities.Product;
import edu.dental.domain.records.ProductMap;

import java.util.*;

public class MyProductMap implements ProductMap {

    private static final byte DEFAULT_CAPACITY = 10;

    private int size;

    protected Item[] entries;


    public MyProductMap() {
        entries = new Item[DEFAULT_CAPACITY];
        size = 0;
    }

    public MyProductMap(Collection<ProductMap.Item> entries) {
        this();
        putAll(entries);
    }

    @Override
    public Product createProduct(String title, int quantity) {
        int i = findIndex(title);
        if (i == -1) {
            throw new NoSuchElementException("type title is not found");
        } else {
            Item entry = entries[i];
            return new Product(entry.getId(), entry.getKey(), (byte) quantity, entry.getValue());
        }
    }

    @Override
    public Integer put(String key, Integer value) {
        if (key == null) {
            throw new NullPointerException("the given key is empty");
        }
        if (size == entries.length) {
            grow();
        }
        int i = findIndex(key);
        if (i > -1) {
            int previous = entries[i].getValue();
            entries[i].setValue(value);
            return previous;
        } else {
            entries[size] = new Item(key.toLowerCase(), value);
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
        return Arrays.stream(toArray()).map(item -> item.price).toList().contains(v);
    }

    @Override
    public Integer get(Object key) {
        String strKey = (String) key;
        int i = findIndex(strKey.toLowerCase());
        if (i == -1) {
            throw new NullPointerException("the specified value is not found");
        } else {
            return entries[i].getValue();
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
            int value = entries[i].getValue();
            entries[i] = null;
            if (size > i) {
                System.arraycopy(entries, i + 1, entries, i, size - i);
            }
            return value;
        }
    }

    @Override
    public void putAll(Map<? extends String, ? extends Integer> m) {
        if (entries == null) {
            entries = new Item[m.size()];
        }
        for (Map.Entry<? extends String, ? extends Integer> entry : m.entrySet()) {
            put(entry.getKey(), entry.getValue());
        }
    }

    @Override
    public void putAll(Collection<ProductMap.Item> c) {
        c.forEach(this::add);
    }

    @Override
    public void clear() {
        if (size != 0) {
            entries = new Item[entries.length];
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
        return Arrays.stream(toArray()).map(v -> v.price).toList();
    }

    @Override
    public Set<Map.Entry<String, Integer>> entrySet() {
        return Set.of(toArray());
    }

    public String[] keysToArray() {
        List<String> list = Arrays.stream(toArray()).map(e -> e.title).toList();
        return list.toArray(new String[]{});
    }

    public Item[] toArray() {
        if (size == 0) {
            throw new NullPointerException("The array of entries is empty.");
        }
        Item[] arr = new Item[size];
        System.arraycopy(entries, 0, arr, 0, size);
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
        return Arrays.stream(toArray()).map(item -> item.title).toList().indexOf(key);
    }

    private void add(ProductMap.Item e) {
        if (e == null) {
            throw new NullPointerException("the given argument is null");
        }
        if (size == entries.length) {
            grow();
        }
        int i = findIndex(e.getKey());
        if (i > -1) {
            entries[i] = (Item) e;
        } else {
            entries[size] = (Item) e;
            size++;
        }
    }

    private void grow() {
        Item[] result = new Item[size << 1];
        System.arraycopy(entries, 0, result, 0, entries.length);
        this.entries = result;
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

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Item item = (Item) o;
            return id == item.id && price == item.price && Objects.equals(title, item.title);
        }

        @Override
        public int hashCode() {
            return Objects.hash(id, title);
        }

        @Override
        public String toString() {
            return "Item{" +
                    "id=" + id +
                    ", title='" + title + '\'' +
                    ", price=" + price +
                    '}';
        }
    }
}
