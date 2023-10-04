package edu.dental.domain.records;

import edu.dental.domain.entities.Product;
import edu.dental.utils.data_structures.MyList;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class ProductMapper implements Map<String, Integer> {

    private static final byte DEFAULT_CAPACITY = 10;

    private int size;

    private Entry[] entries;


    public ProductMapper() {
        entries = new Entry[DEFAULT_CAPACITY];
        size = 0;
    }

    @Override
    public Integer put(String key, Integer price) {
        if (key == null) {
            throw new NullPointerException("key is empty");
        }
        if (size == entries.length) {
            grow();
        }
        int i = findIndex(key);
        if (i > -1) {
            int previous = entries[i].price;
            entries[i].price = price;
            return previous;
        } else {
            entries[size] = new Entry(key.toLowerCase(), price);
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
        int p = (int) value;
        for (Entry e : entries) {
            if (e.price == p) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Integer get(Object key) {
        int i = findIndex((String) key);
        if (i == -1) {
            throw new NullPointerException("value is not found");
        } else {
            return entries[i].price;
        }
    }

    @Override
    public Integer remove(Object key) {
        int i = findIndex((String) key);
        if (i == -1) {
            return null;
        } else {
            size -= 1;
            int value = entries[i].price;
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
            entries = new Entry[m.size()];
        }
        for (Map.Entry<? extends String, ? extends Integer> entry : m.entrySet()) {
            put(entry.getKey(), entry.getValue());
        }
    }

    @Override
    public void clear() {
        if (size != 0) {
            entries = new Entry[entries.length];
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
            prices[i] = entries[i].price;
        }
        return Arrays.asList(prices);
    }

    @Override
    public Set<Map.Entry<String, Integer>> entrySet() {
        return Set.of(entries);
    }

    public String[] keysToArray() {
        String[] result = new String[size];
        for (int i = 0; i < size; i++) {
            result[i] = entries[i].title;
        }
        return result;
    }

    public Product createProduct(String title, int quantity) {
        int i = findIndex(title);
        if (i == -1) {
            throw new NoSuchElementException("type title is not found");
        } else {
            Entry node = entries[i];
            return new Product(node.title, (byte) quantity, node.price);
        }
    }

    /**
     * Create {@link MyList list} of specified {@link Product} objects from database table.
     * @param resultSet The {@link ResultSet} of database values.
     * @return The {@link MyList list} of {@link Product} instances.
     */
    @SuppressWarnings("unused")
    public MyList<Product> instantiateFromDB(ResultSet resultSet) {
        MyList<Product> list = new MyList<>();
        for (Entry t : entries) {
            try {
                int q = resultSet.getInt(t.title);
                if (q != 0) {
                    list.add(createProduct(t.title, q));
                }
            } catch (SQLException ignored) {}
        }
        return list;
    }

    private int findIndex(String title) {
        if (title == null) {
            throw new NullPointerException("param is null");
        }
        if (size < 1) {
            return -1;
        }
        title = title.toLowerCase();
        for (int i = 0; i < size; i++) {
            if (entries[i].title.equals(title)) {
                return i;
            }
        }
        return -1;
    }

    private void grow() {
        Entry[] result = new Entry[size << 1];
        System.arraycopy(entries, 0, result, 0, entries.length);
        this.entries = result;
    }


    public static final class Entry implements Map.Entry<String, Integer> {
        private final String title;
        private int price;

        Entry(String title, int price) {
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
    }
}
