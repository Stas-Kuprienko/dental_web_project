package dental.domain.works;

import dental.domain.data_structures.MyList;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.NoSuchElementException;

public class ProductMapper {

    private static final byte DEFAULT_CAPACITY = 10;

    private int size;

    private Entry[] entries;


    public ProductMapper() {
        entries = new Entry[DEFAULT_CAPACITY];
        size = 0;
    }

    public boolean put(String title, int price) {
        if (title == null) {
            throw new NullPointerException("key is empty");
        }
        if (size == entries.length) {
            grow();
        }
        int i = find(title);
        if (i > -1) {
            entries[i].price = price;
        } else {
            entries[size] = new Entry(title.toLowerCase(), price);
            size++;
        }
        return true;
    }

    public int getPrice(String title) {
        int i = find(title);
        if (i == -1) {
            throw new NullPointerException("value is not found");
        } else {
            return entries[i].price;
        }
    }

    public boolean removeType(String title) {
        int i = find(title);
        if (i == -1) {
            return false;
        } else {
            size -= 1;
            entries[i] = null;
            if (size > i) {
                System.arraycopy(entries, i + 1, entries, i, size - i);
            }
            return true;
        }
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public String[] getAllTitles() {
        String[] result = new String[size];
        for (int i = 0; i < size; i++) {
            result[i] = entries[i].title;
        }
        return result;
    }

    public Product createProduct(String title, int quantity) {
        int i = find(title);
        if (i == -1) {
            throw new NoSuchElementException("type title is not found");
        } else {
            Entry node = entries[i];
            return new Product(node.title, (byte) quantity, node.price);
        }
    }

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

    private int find(String title) {
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
    private static final class Entry {
        final String title;
        int price;

        Entry(String title, int price) {
            this.title = title;
            this.price = price;
        }
    }
}
