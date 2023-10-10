package edu.dental.domain.records.my_work_record_book;

import edu.dental.domain.entities.Product;
import edu.dental.domain.records.Mapper;
import edu.dental.utils.data_structures.MyList;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.NoSuchElementException;

public class ProductMapper extends Mapper {


    public Product createProduct(String title, int quantity) {
        int i = findIndex(title);
        if (i == -1) {
            throw new NoSuchElementException("type title is not found");
        } else {
            Entry node = entries[i];
            return new Product(node.getKey(), (byte) quantity, node.getValue());
        }
    }

    /**
     * Create {@link MyList list} of specified {@link Product} objects from database table.
     * @param resultSet The {@link ResultSet} of database values.
     * @return The {@link MyList list} of {@link Product} instances.
     */
    @SuppressWarnings("unused")
    public MyList<Product> instantiateFromDB(ResultSet resultSet) {
        //TODO ?????????????
        MyList<Product> list = new MyList<>();
        for (Entry e : entries) {
            try {
                int q = resultSet.getInt(e.getKey());
                if (q != 0) {
                    list.add(createProduct(e.getKey(), q));
                }
            } catch (SQLException ignored) {}
        }
        return list;
    }
}
