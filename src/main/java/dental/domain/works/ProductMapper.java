package dental.domain.works;

import java.util.NoSuchElementException;

public class ProductMapper {

    private static final byte DEFAULT_CAPACITY = 10;

    private int size;

    private Node[] table;


    public ProductMapper() {
        table = new Node[DEFAULT_CAPACITY];
        size = 0;
    }

    public boolean put(String type, int price) {
        if (type == null) {
            throw new NullPointerException("key is empty");
        }
        if (size == table.length) {
            grow();
        }
        int i = find(type);
        if (i > -1) {
            table[i].price = price;
        } else {
            table[size] = new Node(type.toLowerCase(), price);
            size++;
        }
        return true;
    }

    public int getPrice(String type) {
        int i = find(type);
        if (i == -1) {
            throw new NullPointerException("value is not found");
        } else {
            return table[i].price;
        }
    }

    public boolean removeType(String type) {
        int i = find(type);
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

    public Product buildProduct(String type, int quantity) {
        int i = find(type);
        if (i == -1) {
            throw new NoSuchElementException("type is not found");
        } else {
            Node node = table[i];
            return new Product(node.type, (byte) quantity, node.price);
        }
    }



    private int find(String type) {
        if (type == null) {
            throw new NullPointerException("param is null");
        }
        if (size < 1) {
            return -1;
        }
        type = type.toLowerCase();
        for (int i = 0; i < size; i++) {
            if (table[i].type.equals(type)) {
                return i;
            }
        }
        return -1;
    }

    private void grow() {
        Node[] result = new Node[size << 1];
        System.arraycopy(table, 0, result, 0, table.length);
        this.table = result;
    }
    private static final class Node {
        final String type;
        int price;

        Node(String type, int price) {
            this.type = type;
            this.price = price;
        }
    }
}
