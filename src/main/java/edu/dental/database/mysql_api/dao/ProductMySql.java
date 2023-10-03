package edu.dental.database.mysql_api.dao;

import edu.dental.database.DatabaseException;
import edu.dental.database.connection.DBConfiguration;
import edu.dental.database.interfaces.DAO;
import edu.dental.domain.entities.Product;
import edu.dental.domain.entities.WorkRecord;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Collection;

public class ProductMySql implements DAO<Product> {

    public final String TABLE;

    private final WorkRecord workRecord;
    private static final String FIELDS = "title, quantity, price";

    public ProductMySql(int userId, WorkRecord workRecord) {
        this.TABLE = DBConfiguration.DATA_BASE + ".product_" + userId;
        this.workRecord = workRecord;
    }

    public void putAll() throws DatabaseException{
        if (workRecord.getProducts() == null || workRecord.getProducts().size() > 0) {
            return;
        }
        for (Product product : workRecord.getProducts()) {
            put(product);
        }
    }

    @Override
    public boolean put(Product object) throws DatabaseException {
        String injections = "?, ".repeat(FIELDS.split(", ").length + 1);
        injections = injections.substring(0, injections.length() - 2);
        String query = String.format(MySqlSamples.INSERT.QUERY, TABLE, "work_id, " + FIELDS, injections);
        try (Request request = new Request(query)) {
            byte i = 1;
            PreparedStatement statement = request.getStatement();
            statement.setInt(i++, workRecord.getId());
            statement.setString(i++, object.title());
            statement.setByte(i++, object.quantity());
            statement.setInt(i, object.price());
            return statement.execute();
        } catch (SQLException e) {
            //TODO loggers
            throw new DatabaseException(e.getMessage(), e.getCause());
        }
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
