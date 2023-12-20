package edu.dental.entities;

import edu.dental.domain.APIManager;

import java.util.Iterator;
import java.util.Map;

public interface ProductMap extends Map<String, Integer> {

    static ProductMap getInstance() {
        return APIManager.INSTANCE.getProductMap();
    }

    static ProductMap getInstance(User user) {
        return APIManager.INSTANCE.getProductMap(user);
    }

    Product createProduct(String title, int quantity);

    /**
     * Associates the specified value with the specified key in this map
     * (optional operation). If the map previously contained a mapping for
     * the key, the old value is replaced by the specified value.
     *
     * @param key key with which the specified value is to be associated
     * @param value value to be associated with the specified key
     * @return the id value of specified {@link ProductMap.Item entry}
     *  if such key exists, or 0 if not.
     *
     * @throws UnsupportedOperationException if the {@code put} operation
     *         is not supported by this map.
     * @throws ClassCastException if the class of the specified key or value
     *         prevents it from being stored in this map.
     * @throws NullPointerException if the specified key is null.
     * @throws IllegalArgumentException if some property of the specified key
     *         or value prevents it from being stored in this map.
     */
    @Override
    Integer put(String key, Integer value);

    /**
     * Associates the specified value with the specified key in this map
     * (optional operation). If the map previously contained a mapping for
     * the key, the old value is replaced by the specified value. Also set
     * {@code id} in current {@link Item entry}.
     *
     * @param title key with which the specified value is to be associated
     * @param price value to be associated with the specified key
     * @param id id to be set in specified entry.
     * @return true if it was success.
     *
     * @throws UnsupportedOperationException if the {@code put} operation
     *         is not supported by this map.
     * @throws ClassCastException if the class of the specified key or value
     *         prevents it from being stored in this map.
     * @throws NullPointerException if the specified key is null.
     * @throws IllegalArgumentException if some property of the specified key
     *         or value prevents it from being stored in this map.
     */
    boolean put(String title, int price, int id);

    /**
     * Removes the mapping for a key from this map if it is present
     * (optional operation).   More formally, if this map contains a mapping
     * from key {@code k} to value {@code v} such that
     * {@code Objects.equals(key, k)}, that mapping
     * is removed.  (The map can contain at most one such mapping.)
     *
     * <p>The map will not contain a mapping for the specified key once the
     * call returns.
     *
     * @param key key whose mapping is to be removed from the map
     * @return the id value of this {@link Item}, or
     *         0 if there was no mapping for {@code key}.
     * @throws UnsupportedOperationException if the {@code remove} operation
     *         is not supported by this map.
     * @throws ClassCastException if the key is of an inappropriate type for
     *         this map.
     * @throws NullPointerException if the specified key is null.
     */
    @Override
    Integer remove(Object key);

    boolean setId(String title, int id);

    int getId(String title);

    String[] keysToArray();

    Item[] toArray();

    Iterator<Item> iterator();

    interface Item extends Map.Entry<String, Integer>, IDHaving {
    }
}
