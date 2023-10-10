package edu.dental.domain.records;

import edu.dental.domain.entities.IDHaving;

import java.util.*;

public class Mapper implements Map<String, Integer> {

    private static final byte DEFAULT_CAPACITY = 10;

    private int size;

    protected Entry[] entries;


    public Mapper() {
        entries = new Entry[DEFAULT_CAPACITY];
        size = 0;
    }

    public Mapper(Entry[] entries) {
        size = entries.length;
        this.entries = entries;
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
            entries[size] = new Entry(key.toLowerCase(), value);
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
        for (Entry e : entries) {
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
            prices[i] = entries[i].getValue();
        }
        return Arrays.asList(prices);
    }

    @Override
    public Set<Map.Entry<String, Integer>> entrySet() {
        return Set.of(toArray());
    }

    public String[] keysToArray() {
        String[] result = new String[size];
        for (int i = 0; i < size; i++) {
            result[i] = entries[i].getKey();
        }
        return result;
    }

    public Entry[] toArray() {
        if (size == 0) {
            throw new NullPointerException("The array of entries is empty.");
        }
        Entry[] arr = new Entry[size];
        System.arraycopy(entries, 0, arr, 0, size);
        return arr;
    }

    protected int findIndex(String key) {
        if (key == null) {
            throw new NullPointerException("the given argument is null");
        }
        if (size < 1) {
            return -1;
        }
        key = key.toLowerCase();
        for (int i = 0; i < size; i++) {
            if (entries[i].getKey().equals(key)) {
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


    public static class Entry implements Map.Entry<String, Integer>, IDHaving {

        private int id;
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
