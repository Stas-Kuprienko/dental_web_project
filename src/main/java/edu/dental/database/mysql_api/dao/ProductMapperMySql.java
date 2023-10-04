package edu.dental.database.mysql_api.dao;

import edu.dental.database.DatabaseException;
import edu.dental.database.connection.DBConfiguration;
import edu.dental.database.interfaces.DAO;
import edu.dental.domain.entities.User;
import edu.dental.domain.records.ProductMapper;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Collection;

public class ProductMapperMySql implements DAO<ProductMapper.Entry> {

    public static final String FIELDS = "id, title, price";

    public final String TABLE;

    private final User user;

    public ProductMapperMySql(User user) {
        this.user = user;
        this.TABLE = DBConfiguration.DATA_BASE + ".product_map_" + user.getId();
    }

    @Override
    public boolean put(ProductMapper.Entry entry) throws DatabaseException {
        String injections = "?, ".repeat(FIELDS.split(", ").length - 1);
        injections = "DEFAULT, " + injections.substring(0, injections.length() - 2);
        String query = String.format(MySqlSamples.INSERT.QUERY, TABLE, FIELDS, injections);
        try (Request request = new Request(query)) {
            byte i = 1;
            PreparedStatement statement = request.getStatement();
            statement.setString(i++, entry.getKey());
            statement.setInt(i, entry.getValue());
            return statement.execute();
        } catch (SQLException e) {
            throw new DatabaseException(e.getMessage(), e.getCause());
        }
    }

    @Override
    public Collection<ProductMapper.Entry> getAll() throws DatabaseException {
        return null;
    }

    @Override
    public ProductMapper.Entry get(int id) throws DatabaseException {
        return null;
    }

    @Override
    public Collection<ProductMapper.Entry> search(Object value1, Object value2) throws DatabaseException {
        return null;
    }

    @Override
    public boolean edit(ProductMapper.Entry object) throws DatabaseException {
        return false;
    }

    @Override
    public boolean delete(int id) throws DatabaseException {
        return false;
    }
}
