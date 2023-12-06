package edu.dental.domain.records.my_work_record_book;

import edu.dental.domain.entities.Product;
import edu.dental.domain.records.ProductMap;

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

    @Override
    public Product createProduct(String title, int quantity) {
        if (quantity > 32) {
            throw new IllegalArgumentException("The quantity value ('"
                    + quantity + "') is incorrect - cannot be more than 32 teeth.");
        }
        if (title == null || title.isEmpty()) {
            throw new NullPointerException("the given key is null or empty");
        }
        Item entry = getItem(title);
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
        Item entry;
        try {
            entry = getItem(key);
            int id = entry.getId();
            entry.setValue(value);
            return id;
        } catch (NullPointerException | NoSuchElementException ignored) {
            int index = getIndex(key);
            for (;; index = (index + 1) % CAPACITY) {
                if (entries[index] == null) {
                    entry = new Item(key, value);
                    entries[index] = entry;
                    if (head != null) {
                        head.previous = entry;
                        entry.next = head;
                    }
                    head = entry;
                    size++;
                    return 0;
                }
            }
        }
    }

    @Override
    public boolean put(String key, int value, int id) {
        if (key == null || key.isEmpty()) {
            throw new NullPointerException("the given key is null or empty");
        }
        if (isFilled()) {
            grow();
        }
        key = key.toLowerCase();
        Item entry;
        try {
            entry = getItem(key);
            entry.setValue(value);
            entry.setId(id);
            return true;
        } catch (NullPointerException | NoSuchElementException ignored) {
            int index = getIndex(key);
            for (;; index = (index + 1) % CAPACITY) {
                if (entries[index] == null) {
                    entry = new Item(key, value);
                    entry.setId(id);
                    entries[index] = entry;
                    if (head != null) {
                        head.previous = entry;
                        entry.next = head;
                    }
                    head = entry;
                    size++;
                    return true;
                }
            }
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
        String strKey = ((String) key).toLowerCase();
        return Arrays.stream(toArray()).anyMatch(e -> e.getKey().equals(strKey));
    }

    @Override
    public boolean containsValue(Object value) {
        return Arrays.stream(toArray()).anyMatch(e -> e.getValue().equals(value));
    }

    @Override
    public Integer get(Object key) {
        String strKey = (String) key;
        if (strKey == null || strKey.isEmpty()) {
            throw new NullPointerException("the given key is null or empty");
        }
        return getItem(strKey).price;
    }

    @Override
    public Integer remove(Object key) {
        String strKey = (String) key;
        if (strKey == null || strKey.isEmpty()) {
            throw new NullPointerException("the given key is null or empty");
        }
        strKey = strKey.toLowerCase();
        int index = getIndex(strKey);
        Item e;
        for (int i = 0; i < size; i++) {
            e = entries[index];
            if (e != null && e.title.equals(strKey)) {
                entries[index] = null;
                int id = e.getId();
                if (head == e) {
                    head = e.next;
                } else if (e.previous != null) {
                    e.previous.next = e.next;
                }
                if (e.next != null) {
                    e.next.previous = e.previous;
                }
                size -= 1;
                return id;
            }
            index = (index + 1) % CAPACITY;
        } throw new NullPointerException("the specified entry ('"
                + strKey + "') is not found");
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
    public boolean setId(String title, int id) {
        try {
            getItem(title).setId(id);
            return true;
        } catch (NullPointerException | NoSuchElementException ignored) {
            return false;
        }
    }

    @Override
    public int getId(String title) {
        return getItem(title).getId();
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
        Collection<Integer> list = new ArrayList<>();
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
        Arrays.stream(toArray()).map(e -> e.title).toList().toArray(arr);
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
        Arrays.sort(arr);
        return arr;
    }

    public Iterator iterator() {
        return this.new Iterator();
    }

    @Override
    public String toString() {
        return "MyProductMap{" +
                "entries=" + Arrays.toString(toArray()) +
                '}';
    }

    private int getIndex(String key) {
        key = key.toLowerCase();
        int index = key.hashCode() % CAPACITY;
        return index < 0 ? index * (-1) : index;
    }

    private boolean isFilled() {
        int max = 75;
        return (size*100)/(CAPACITY*100) > max;
    }

    private Item getItem(String key) {
        if (key == null || key.isEmpty()) {
            throw new NullPointerException("the given key is null or empty");
        }
        key = key.toLowerCase();
        int index = getIndex(key);
        Item e;
        for (int i = 0; i < size; i++) {
            e = entries[index];
            if (e != null && e.title.equals(key)) {
                return e;
            }
            index = (index + 1) % CAPACITY;
        } throw new NoSuchElementException("the specified entry ('"
                + key + "') is not found");
    }

    private void grow() {
        Item[] buff = new Item[size];
        System.arraycopy(entries, 0, buff, 0, size);
        entries = new Item[size << 1];
        head = null;
        for (Item i : buff) {
            put(i.title, i.price, i.id);
        }
    }


    public static class Item implements ProductMap.Item, Comparable<Item> {

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
            return "\n  - Item{" +
                    "id=" + id +
                    ", title='" + title + '\'' +
                    ", price=" + price +
                    '}';
        }

        @Override
        public int compareTo(Item o) {
            return Integer.compare(this.id, o.id);
        }
    }

    public final class Iterator implements java.util.Iterator<ProductMap.Item> {

        private Item entry;

        private Iterator() {
            if (head == null) {
                throw new NullPointerException("this map is empty");
            }
            this.entry = head;
        }

        @Override
        public boolean hasNext() {
            return entry != null;
        }

        @Override
        public Item next() {
            Item e = entry;
            entry = entry.next;
            return e;
        }
    }
}
