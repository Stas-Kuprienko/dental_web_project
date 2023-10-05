package edu.dental.database.mysql_api.dao;

import edu.dental.database.DatabaseException;
import edu.dental.database.connection.DBConfiguration;
import edu.dental.database.interfaces.DAO;
import edu.dental.domain.entities.Product;
import edu.dental.domain.entities.User;
import edu.dental.domain.entities.WorkRecord;
import edu.dental.utils.data_structures.MyList;

import java.io.IOException;
import java.sql.*;
import java.util.Collection;

public class WorkRecordMySql implements DAO<WorkRecord> {

    public final String TABLE;
    private final User user;

    private static final String FIELDS = "id, patient, clinic, accepted, complete, closed, paid, photo, comment";

    public WorkRecordMySql(User user) {
        this.user = user;
        this.TABLE = DBConfiguration.DATA_BASE + ".work_record_" + user.getId();
    }

    @Override
    public boolean put(WorkRecord object) throws DatabaseException {
        String injections = "?, ".repeat(FIELDS.split(", ").length - 1);
        injections = "DEFAULT, " + injections.substring(0, injections.length() - 2);
        String query = String.format(MySqlSamples.INSERT.QUERY, TABLE, FIELDS, injections);
        try (Request request = new Request(query)) {
            byte i = 1;
            PreparedStatement statement = request.getStatement();
            statement.setString(i++, object.getPatient());
            statement.setString(i++, object.getClinic());
            statement.setDate(i++, Date.valueOf(object.getAccepted()));
            statement.setDate(i++, Date.valueOf(object.getComplete()));
            statement.setBoolean(i++, object.isClosed());
            statement.setBoolean(i++, object.isPaid());
            Blob photo = request.createBlob();
            photo.setBytes(1, object.getPhoto());
            statement.setBlob(i++, photo);
            statement.setString(i, object.getComment());
            statement.executeUpdate();
            photo.free();
            request.setID(object);
            return new ProductMySql(user.getId(), object.getId()).putAll(object.getProducts());
        } catch (SQLException e) {
            //TODO loggers
            throw new DatabaseException(e.getMessage(), e.getCause());
        }
    }

    @Override
    public Collection<WorkRecord> getAll() throws DatabaseException {
        String query = String.format(MySqlSamples.SELECT_ALL.QUERY, "*", TABLE);
        MyList<WorkRecord> workRecords;
        try (Request request = new Request(query)) {
            ResultSet resultSet = request.getStatement().executeQuery();
            workRecords = (MyList<WorkRecord>) new WorkRecordInstantiation(resultSet).build();
        } catch (SQLException | IOException e) {
            //TODO loggers
            throw new DatabaseException(e.getMessage(), e.getCause());
        }
        return workRecords;
    }

    @Override
    public WorkRecord get(int id) throws DatabaseException {
        String query = String.format(MySqlSamples.SELECT_WHERE.QUERY, "*", TABLE, "id = ?");
        try (Request request = new Request(query)) {
            request.getStatement().setInt(1, id);
            ResultSet resultSet = request.getStatement().executeQuery();
            MyList<WorkRecord> list = (MyList<WorkRecord>) new WorkRecordInstantiation(resultSet).build();
            return list.get(0);
        } catch (SQLException | IOException | NullPointerException e) {
            //TODO loggers
            throw new DatabaseException(e.getMessage(), e.getCause());
        }
    }

    @Override
    public MyList<WorkRecord> search(Object value1, Object value2) throws DatabaseException {
        String where = "patient = ? AND clinic = ?";
        String query = String.format(MySqlSamples.SELECT_WHERE.QUERY, "*", TABLE, where);
        try (Request request = new Request(query)) {
            request.getStatement().setString(1, (String) value1);
            request.getStatement().setString(2, (String) value2);
            ResultSet resultSet = request.getStatement().executeQuery();
            return (MyList<WorkRecord>) new WorkRecordInstantiation(resultSet).build();
        } catch (SQLException | IOException | NullPointerException | ClassCastException e) {
            //TODO loggers
            throw new DatabaseException(e.getMessage(), e.getCause());
        }
    }

    @Override
    public boolean edit(WorkRecord object) throws DatabaseException {
        StringBuilder sets = new StringBuilder();
        String[] fields = FIELDS.split(", ");
        for (int i = 1; i < fields.length; i++) {
            sets.append(fields[i]).append("=?,");
        } sets.deleteCharAt(sets.length()-1);
        String query = String.format(MySqlSamples.UPDATE.QUERY, TABLE, sets,"id = ?");
        try (Request request = new Request(query)) {
            byte i = 1;
            request.getStatement().setString(i++, object.getPatient());
            request.getStatement().setString(i++, object.getClinic());
            request.getStatement().setDate(i++, Date.valueOf(object.getAccepted()));
            request.getStatement().setDate(i++, Date.valueOf(object.getComplete()));
            request.getStatement().setBoolean(i++, object.isClosed());
            request.getStatement().setBoolean(i++, object.isPaid());
            Blob photo = request.createBlob();
            photo.setBytes(1, object.getPhoto());
            request.getStatement().setBlob(i++, photo);
            request.getStatement().setString(i++, object.getComment());
            request.getStatement().setInt(i, object.getId());
            return new ProductMySql(user.getId(), object.getId()).editAll(object.getProducts())
                    && request.getStatement().execute();
        } catch (SQLException e) {
            //TODO loggers
            throw new DatabaseException(e.getMessage(), e.getCause());
        }
    }

    @Override
    public boolean delete(int id) throws DatabaseException {
        String query = String.format(MySqlSamples.DELETE.QUERY, TABLE, "id = ?");
        try (Request request = new Request(query)) {
            request.getStatement().setInt(1, id);
            int workId = request.getStatement().executeUpdate();
            return new ProductMySql(user.getId(), workId).delete(workId);
        } catch (SQLException e) {
            //TODO loggers
            throw new DatabaseException(e.getMessage(), e.getCause());
        }
    }


    protected class WorkRecordInstantiation implements Instantiating<WorkRecord> {

        private final MyList<WorkRecord> recordsList;
        private final ResultSet resultSet;

        public WorkRecordInstantiation(ResultSet resultSet) {
            this.resultSet = resultSet;
            this.recordsList = new MyList<>();
        }

        @Override
        public Collection<WorkRecord> build() throws SQLException, IOException, DatabaseException {
            try (resultSet) {
                String[] fields = FIELDS.split(", ");
                while (resultSet.next()) {
                    byte i = 0;
                    WorkRecord workRecord = new WorkRecord();
                    workRecord.setId(resultSet.getInt(fields[i++]));
                    workRecord.setPatient(resultSet.getString(fields[i++]));
                    workRecord.setClinic(resultSet.getString(fields[i++]));
                    workRecord.setAccepted(resultSet.getDate(fields[i++]).toLocalDate());
                    workRecord.setComplete(resultSet.getDate(fields[i++]).toLocalDate());
                    workRecord.setClosed(resultSet.getBoolean(fields[i++]));
                    workRecord.setPaid(resultSet.getBoolean(fields[i++]));
                    Blob blob = resultSet.getBlob(fields[i++]);
                    workRecord.setPhoto(blob.getBytes(1, (int) blob.length()));
                    blob.free();
                    workRecord.setComment(resultSet.getString(fields[i]));
                    MyList<Product> products = (MyList<Product>)
                            new ProductMySql(user.getId(), workRecord.getId()).getAll();
                    workRecord.setProducts(products);
                    recordsList.add(workRecord);
                }
            }
            return this.recordsList;
        }
    }
}
