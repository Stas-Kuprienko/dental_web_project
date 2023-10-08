package edu.dental.database.mysql_api.dao;

import edu.dental.database.DatabaseException;
import edu.dental.database.connection.DBConfiguration;
import edu.dental.database.interfaces.WorkRecordDAO;
import edu.dental.domain.entities.Product;
import edu.dental.domain.entities.User;
import edu.dental.domain.entities.WorkRecord;
import edu.dental.utils.data_structures.MyList;

import java.io.IOException;
import java.sql.*;
import java.util.Arrays;
import java.util.Collection;

public class WorkRecordMySql implements WorkRecordDAO {

    public final String TABLE;
    private final User user;

    private static final String FIELDS = "id, patient, clinic, accepted, complete, closed, paid, photo, comment";

    public WorkRecordMySql(User user) {
        this.user = user;
        this.TABLE = DBConfiguration.DATA_BASE + ".work_record_" + user.getId();
    }

    public WorkRecordMySql(User user, String yearMonth) {
        this.user = user;
        this.TABLE = DBConfiguration.DATA_BASE + ".work_record_" + user.getId() + "_" + yearMonth;
    }

    @Override
    public boolean putAll(Collection<WorkRecord> list) throws DatabaseException {
        if (list == null || list.isEmpty()) {
            throw new DatabaseException("The given argument is null or empty.");
        }
        try (Request request = new Request()){
            Statement statement = request.getStatement();
            for (WorkRecord wr : list) {
                String query = buildQuery(wr);
                statement.addBatch(query);
            }
            return statement.executeBatch().length == list.size();
        } catch (SQLException e) {
            throw new DatabaseException(e.getMessage(), e.getCause());
        }
    }

    @Override
    public boolean put(WorkRecord object) throws DatabaseException {
        String injections = "?, ".repeat(FIELDS.split(", ").length - 1);
        injections = "DEFAULT, " + injections.substring(0, injections.length() - 2);
        String query = String.format(MySqlSamples.INSERT.QUERY, TABLE, FIELDS, injections);
        try (Request request = new Request(query)) {
            byte i = 1;
            PreparedStatement statement = request.getPreparedStatement();
            statement.setString(i++, object.getPatient());
            statement.setString(i++, object.getClinic());
            statement.setDate(i++, Date.valueOf(object.getAccepted()));
            statement.setDate(i++, Date.valueOf(object.getComplete()));
            statement.setBoolean(i++, object.isClosed());
            statement.setBoolean(i++, object.isPaid());
            Blob photo = request.createBlob();
            if (object.getPhoto() != null) {
                photo.setBytes(1, object.getPhoto());
                statement.setBlob(i++, photo);
            } else {
                statement.setNull(i++, Types.BLOB);
            }
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
            ResultSet resultSet = request.getPreparedStatement().executeQuery();
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
            request.getPreparedStatement().setInt(1, id);
            ResultSet resultSet = request.getPreparedStatement().executeQuery();
            MyList<WorkRecord> list = (MyList<WorkRecord>) new WorkRecordInstantiation(resultSet).build();
            return list.get(0);
        } catch (SQLException | IOException | NullPointerException e) {
            //TODO loggers
            throw new DatabaseException(e.getMessage(), e.getCause());
        }
    }

    @Override
    public MyList<WorkRecord> search(Object... args) throws DatabaseException {
        String where = "patient = ? AND clinic = ?";
        String query = String.format(MySqlSamples.SELECT_WHERE.QUERY, "*", TABLE, where);
        try (Request request = new Request(query)) {
            request.getPreparedStatement().setString(1, (String) args[0]);
            request.getPreparedStatement().setString(2, (String) args[1]);
            ResultSet resultSet = request.getPreparedStatement().executeQuery();
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
            request.getPreparedStatement().setString(i++, object.getPatient());
            request.getPreparedStatement().setString(i++, object.getClinic());
            request.getPreparedStatement().setDate(i++, Date.valueOf(object.getAccepted()));
            request.getPreparedStatement().setDate(i++, Date.valueOf(object.getComplete()));
            request.getPreparedStatement().setBoolean(i++, object.isClosed());
            request.getPreparedStatement().setBoolean(i++, object.isPaid());
            Blob photo = request.createBlob();
            photo.setBytes(1, object.getPhoto());
            request.getPreparedStatement().setBlob(i++, photo);
            request.getPreparedStatement().setString(i++, object.getComment());
            request.getPreparedStatement().setInt(i, object.getId());
            return new ProductMySql(user.getId(), object.getId()).editAll(object.getProducts())
                    && request.getPreparedStatement().execute();
        } catch (SQLException e) {
            //TODO loggers
            throw new DatabaseException(e.getMessage(), e.getCause());
        }
    }

    @Override
    public boolean delete(int id) throws DatabaseException {
        String query = String.format(MySqlSamples.DELETE.QUERY, TABLE, "id = ?");
        try (Request request = new Request(query)) {
            request.getPreparedStatement().setInt(1, id);
            int workId = request.getPreparedStatement().executeUpdate();
            return new ProductMySql(user.getId(), workId).delete(workId);
        } catch (SQLException e) {
            //TODO loggers
            throw new DatabaseException(e.getMessage(), e.getCause());
        }
    }

    private String buildQuery(WorkRecord wr) {
        String values = "DEFAULT, '%s', '%s', '%s', '%s', %s, %s, %s, %s";
        values = String.format(values, wr.getPatient(), wr.getClinic(), wr.getAccepted(),
                wr.getComplete(), wr.isClosed(), wr.isPaid(), Arrays.toString(wr.getPhoto()), wr.getComment());
        return String.format(MySqlSamples.INSERT_BATCH.QUERY, TABLE, values);
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
