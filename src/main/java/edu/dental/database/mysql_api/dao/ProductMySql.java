package edu.dental.database.mysql_api.dao;

import edu.dental.database.interfaces.DAO;
import edu.dental.domain.entities.Product;

import java.util.Collection;

public class ProductMySql implements DAO<Product> {
    @Override
    public boolean put(Product object) {
        return false;
    }

    @Override
    public Collection<Product> getAll() {
        return null;
    }

    @Override
    public Product get(int id) {
        return null;
    }

    @Override
    public Product search(Object value1, Object value2) {
        return null;
    }

    @Override
    public boolean edit(Product object) {
        return false;
    }

    @Override
    public boolean delete(int id) {
        return false;
    }
}
