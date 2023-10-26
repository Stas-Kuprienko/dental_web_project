package edu.dental.domain.records.my_work_record_book;

import edu.dental.domain.entities.Product;
import edu.dental.domain.records.ProductMap;
import edu.dental.utils.data_structures.MyList;

import java.util.*;

public class MyProductMap implements ProductMap {

    private static final byte CAPACITY = 16;

    private int size;

    protected Item[] entries;

    private Item head;


    public MyProductMap() {
        entries = new Item[CAPACITY];
        size = 0;
    }

    public MyProductMap(Collection<ProductMap.Item> entries) {
        this();
        putAll(entries);
    }

    @Override
    public Product createProduct(String title, int quantity) {
        if (quantity > 32) {
            throw new IllegalArgumentException("The quantity value is incorrect - cannot be more than 32 teeth.");
        }
        if (title == null || title.isEmpty()) {
            throw new NullPointerException("the given key is null or empty");
        }
        Item entry = entries[getIndex(title)];
        return new Product(entry.getId(), entry.getKey(), (byte) quantity, entry.getValue());

    }

    @Override
    public Integer put(String key, Integer value) {
        if (key == null || key.isEmpty()) {
            throw new NullPointerException("the given key is null or empty");
        }
        if (isFilled()) {
            grow();
        }
        key = key.toLowerCase();
        int index = getIndex(key);
        Item entry;
        if (entries[index] != null && entries[index].title.equals(key)) {
            entry = entries[index];
            int toReturn = entry.getValue();
            entry.setValue(value);
            return toReturn;
        }
        for (;;) {
            if (entries[index] == null) {
                entry = new Item(key, value);
                entries[index] = entry;
                if (head != null) {
                    head.previous = entry;
                    entry.next = head;
                }
                head = entry;
                size++;
                return null;
            }
            index = (index + 1) % CAPACITY;
        }
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public boolean containsKey(Object key) {
        String strKey = (String) key;
        int index = getIndex(strKey.toLowerCase());
        for (;;) {
            if (entries[index] == null) {
                return false;
            } else {
                if (entries[index].title.equals(strKey)) {
                    return true;
                }
                index = (index + 1) % CAPACITY;
            }
        }
    }

    @Override
    public boolean containsValue(Object value) {
        //TODO
        int v = (int) value;
        return Arrays.stream(toArray()).anyMatch(e -> e.getValue().equals(value));
    }

    @Override
    public Integer get(Object key) {
        String strKey = (String) key;
        if (strKey == null || strKey.isEmpty()) {
            throw new NullPointerException("the given key is null or empty");
        }
        int index = getIndex(strKey.toLowerCase());
        Item entry = entries[index];
        for (;;) {
            if (entry == null) {
                throw new NullPointerException("the specified value is not found");
            }
            if (entry.title.equals(strKey)) {
                return entry.getValue();
            }
            index = (index + 1) % CAPACITY;
            entry = entries[index];
        }
    }

    @Override
    public Integer remove(Object key) {
        String strKey = (String) key;
        int index = getIndex(strKey.toLowerCase());
        if (entries[index] == null) {
            return null;
        } else {
            Item entry = entries[index];
            entries[index] = null;
            int value = entry.getValue();
            if (entry.next != null) {
                entry.next.previous = entry.previous;
            }
            if (entry.previous != null) {
                entry.previous.next = entry.next;
            }
            size -= 1;
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
    public ProductMap.Item getEntry(String title) {
        //TODO DELETE
        try {
            return entries[findIndex(title)];
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new NoSuchElementException(title + " - entry is not found");
        }
    }

    @Override
    public void putAll(Collection<ProductMap.Item> c) {
        //TODO
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
            throw new NullPointerException("The array of entries is empty.");
        }
        MyList<Integer> list = new MyList<>();
        Item entry = head;
        while (entry != null) {
            list.add(entry.price);
            entry = entry.next;
        }
        return list;
    }

    @Override
    public Set<Map.Entry<String, Integer>> entrySet() {
        return Set.of(toArray());
    }

    @Override
    public String[] keysToArray() {
        if (size == 0) {
            throw new NullPointerException("The array of entries is empty.");
        }
        String[] arr = new String[size];
        Item entry = head;
        for (int i = 0; entry != null; i++) {
            arr[i] = entry.title;
            entry = entry.next;
        }
        return arr;
    }

    @Override
    public Item[] toArray() {
        if (size == 0) {
            throw new NullPointerException("The array of entries is empty.");
        }
        Item[] arr = new Item[size];
        Item entry = head;
        for (int i = 0; entry != null; i++) {
            arr[i] = entry;
            entry = entry.next;
        }
        return arr;
    }

    @Override
    public String toString() {
        return "MyProductMap{" +
                "entries=" + Arrays.toString(toArray()) +
                '}';
    }

    private int getIndex(String key) {
        return key.hashCode() % CAPACITY;
    }

    private boolean isFilled() {
        int max = 75;
        return (size*100)/(CAPACITY*100) > max;
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
        //TODO
        Item[] result = new Item[size << 1];
        System.arraycopy(entries, 0, result, 0, entries.length);
        this.entries = result;
    }


    public static class Item implements ProductMap.Item {

        private int id;
        private final String title;
        private int price;

        private Item next;
        private Item previous;

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
