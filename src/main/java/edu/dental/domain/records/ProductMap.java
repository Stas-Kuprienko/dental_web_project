package edu.dental.domain.records;

import edu.dental.domain.entities.IDHaving;
import edu.dental.domain.entities.Product;

import java.util.Collection;
import java.util.Map;

public interface ProductMap extends Map<String, Integer> {

    Product createProduct(String title, int quantity);

    void putAll(Collection<Item> c);

    String[] keysToArray();

    Item[] toArray();

    interface Item extends Map.Entry<String, Integer>, IDHaving {
    }
}
