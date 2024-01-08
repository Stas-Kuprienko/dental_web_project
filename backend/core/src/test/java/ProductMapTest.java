import edu.dental.entities.Product;
import edu.dental.entities.ProductMap;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;

import java.util.NoSuchElementException;

import static org.junit.Assert.*;

public class ProductMapTest {
    private ProductMap productMap;

    @Before
    public void setUp() {
        productMap = ProductMap.getInstance();
        productMap.put("key1", 11, 1);
        productMap.put("key2", 12, 2);
        productMap.put("key3", 13, 3);
    }

    @Test
    public void testCreateProduct() {
        String title = "key1";
        int quantity = 10;

        Product product = productMap.createProduct(title, quantity);

        Assertions.assertEquals(title, product.title());
        Assertions.assertEquals(quantity, product.quantity());
    }

    @Test
    public void testGetKeys() {
        String[] expectedKeys = {"key1", "key2", "key3"};
        String[] actualKeys = productMap.keysToArray();
        assertArrayEquals(expectedKeys, actualKeys);
    }

    @Test
    public void testSize() {
        assertEquals(3, productMap.size());
        productMap.put("key4", 14, 4);
        assertEquals(4, productMap.size());
        productMap.remove("key4");
        assertEquals(3, productMap.size());
    }

    @Test
    public void testUpdate() {
        productMap.put("key2", 25);
        int value = productMap.get("key2");
        assertEquals(25, value);
    }

    @Test
    public void testRemove() {
        productMap.remove("key3");
        assertEquals(2, productMap.size());
        assertThrows(NoSuchElementException.class, () -> productMap.get("key3"));
    }

    @Test
    public void testIsEmpty() {
        assertFalse(productMap.isEmpty());
        productMap = ProductMap.getInstance();
        assertTrue(productMap.isEmpty());
    }
}