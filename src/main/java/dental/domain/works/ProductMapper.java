package dental.domain.works;

import dental.domain.MyList;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.NoSuchElementException;

public class ProductMapper {

    private static final byte DEFAULT_CAPACITY = 10;

    private int size;

    private TypeNode[] table;


    public ProductMapper() {
        table = new TypeNode[DEFAULT_CAPACITY];
        size = 0;
    }

    public boolean put(String title, int price) {
        if (title == null) {
            throw new NullPointerException("key is empty");
        }
        if (size == table.length) {
            grow();
        }
        int i = find(title);
        if (i > -1) {
            table[i].price = price;
        } else {
            table[size] = new TypeNode(title.toLowerCase(), price);
            size++;
        }
        return true;
    }

    public int getPrice(String title) {
        int i = find(title);
        if (i == -1) {
            throw new NullPointerException("value is not found");
        } else {
            return table[i].price;
        }
    }

    public boolean removeType(String title) {
        int i = find(title);
        if (i == -1) {
            return false;
        } else {
            size -= 1;
            table[i] = null;
            if (size > i) {
                System.arraycopy(table, i + 1, table, i, size - i);
            }
            return true;
        }
    }

    public Product buildProduct(String title, int quantity) {
        int i = find(title);
        if (i == -1) {
            throw new NoSuchElementException("type title is not found");
        } else {
            TypeNode node = table[i];
            return new Product(node.title, (byte) quantity, node.price);
        }
    }

    public MyList<Product> instantiateFromDB(ResultSet resultSet) {
        MyList<Product> list = new MyList<>();
        for (TypeNode t : table) {
            try {
                int q = resultSet.getInt(t.title);
                if (q != 0) {
                    list.add(buildProduct(t.title, q));
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
            if (table[i].title.equals(title)) {
                return i;
            }
        }
        return -1;
    }

    private void grow() {
        TypeNode[] result = new TypeNode[size << 1];
        System.arraycopy(table, 0, result, 0, table.length);
        this.table = result;
    }
    private static final class TypeNode {
        final String title;
        int price;

        TypeNode(String title, int price) {
            this.title = title;
            this.price = price;
        }
    }
}
