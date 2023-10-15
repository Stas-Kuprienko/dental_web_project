package edu.dental.domain.records;

import edu.dental.domain.entities.IDHaving;
import edu.dental.domain.entities.Product;

import java.util.Map;

public interface ProductMap extends Map<String, Integer> {

    Product createProduct(String title, int quantity);

    String getTitleByID(int id);

    int getIDByTitle(String title);

    String[] keysToArray();

    Item[] toArray();

    interface Item extends Map.Entry<String, Integer>, IDHaving {}
}
