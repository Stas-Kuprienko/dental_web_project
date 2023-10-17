package edu.dental.database.mysql_api.dao;

import edu.dental.database.DatabaseException;
import edu.dental.database.TableInitializer;
import edu.dental.database.dao.WorkRecordDAO;
import edu.dental.domain.entities.I_WorkRecord;
import edu.dental.domain.entities.Product;
import edu.dental.domain.entities.User;
import edu.dental.domain.entities.WorkRecord;
import edu.dental.utils.data_structures.MyList;

import java.io.IOException;
import java.sql.*;
import java.util.Arrays;
import java.util.Collection;

public class WorkRecordMySql implements WorkRecordDAO {

    public final String TABLE = TableInitializer.WORK_RECORD;
    private final User user;

    private static final String FIELDS = "id, patient, clinic, accepted, complete, status, photo, comment, report_id";

    public WorkRecordMySql(User user) {
        this.user = user;
    }


    @Override
    public boolean putAll(Collection<I_WorkRecord> list) throws DatabaseException {
        if (list == null || list.isEmpty()) {
            throw new DatabaseException("The given argument is null or empty.");
        }
        try (Request request = new Request()){
            Statement statement = request.getStatement();
            for (I_WorkRecord wr : list) {
                String query = buildQuery((WorkRecord) wr);
                statement.addBatch(query);
            }
            return statement.executeBatch().length == list.size();
        } catch (SQLException e) {
            throw new DatabaseException(e.getMessage(), e.getCause());
        }
    }

    @Override
    public boolean put(I_WorkRecord object) throws DatabaseException {
        String injections = "?, ".repeat(FIELDS.split(", ").length - 1);
        injections = "DEFAULT, " + injections.substring(0, injections.length() - 2);
        String query = String.format(MySqlSamples.INSERT.QUERY, TABLE, FIELDS, injections);
        try (Request request = new Request(query)) {
            byte i = 1;
            WorkRecord workRecord = (WorkRecord) object;
            PreparedStatement statement = request.getPreparedStatement();
            statement.setString(i++, workRecord.getPatient());
            statement.setString(i++, workRecord.getClinic());
            statement.setDate(i++, Date.valueOf(workRecord.getAccepted()));
            statement.setDate(i++, Date.valueOf(workRecord.getComplete()));
            statement.setString(i++, String.valueOf(workRecord.getStatus()));
            Blob photo = request.createBlob();
            if (workRecord.getPhoto() != null) {
                photo.setBytes(1, workRecord.getPhoto());
                statement.setBlob(i++, photo);
            } else {
                statement.setNull(i++, Types.BLOB);
            }
            statement.setString(i, workRecord.getComment());
            statement.executeUpdate();
            photo.free();
            request.setID(workRecord);
            return new ProductMySql(workRecord.getId()).putAll(workRecord.getProducts());
        } catch (SQLException e) {
            //TODO loggers
            throw new DatabaseException(e.getMessage(), e.getCause());
        }
    }

    @Override
    public Collection<I_WorkRecord> getAll() throws DatabaseException {
        String where = TableInitializer.WORK_RECORD + ".user_id = " + user.getId();
        String query = String.format(MySqlSamples.SELECT_WORK_RECORD.QUERY, where);
        MyList<I_WorkRecord> workRecords;
        try (Request request = new Request(query)) {
            ResultSet resultSet = request.getPreparedStatement().executeQuery();
            workRecords = (MyList<I_WorkRecord>) new WorkRecordInstantiation(resultSet).build();
        } catch (SQLException | IOException | ClassCastException e) {
            //TODO loggers
            throw new DatabaseException(e.getMessage(), e.getCause());
        }
        return workRecords;
    }

    @Override
    public I_WorkRecord get(int id) throws DatabaseException {
        String query = String.format(MySqlSamples.SELECT_WHERE.QUERY, "*", TABLE, "id = ?");
        try (Request request = new Request(query)) {
            request.getPreparedStatement().setInt(1, id);
            ResultSet resultSet = request.getPreparedStatement().executeQuery();
            MyList<I_WorkRecord> list = (MyList<I_WorkRecord>) new WorkRecordInstantiation(resultSet).build();
            return list.get(0);
        } catch (SQLException | IOException | NullPointerException e) {
            //TODO loggers
            throw new DatabaseException(e.getMessage(), e.getCause());
        }
    }

    @Override
    public MyList<I_WorkRecord> search(Object... args) throws DatabaseException {
        String where = "patient = ? AND clinic = ?";
        String query = String.format(MySqlSamples.SELECT_WHERE.QUERY, "*", TABLE, where);
        try (Request request = new Request(query)) {
            request.getPreparedStatement().setString(1, (String) args[0]);
            request.getPreparedStatement().setString(2, (String) args[1]);
            ResultSet resultSet = request.getPreparedStatement().executeQuery();
            return (MyList<I_WorkRecord>) new WorkRecordInstantiation(resultSet).build();
        } catch (SQLException | IOException | NullPointerException | ClassCastException e) {
            //TODO loggers
            throw new DatabaseException(e.getMessage(), e.getCause());
        }
    }

    @Override
    public boolean edit(I_WorkRecord object) throws DatabaseException {
        StringBuilder sets = new StringBuilder();
        String[] fields = FIELDS.split(", ");
        for (int i = 1; i < fields.length; i++) {
            sets.append(fields[i]).append("=?,");
        } sets.deleteCharAt(sets.length()-1);
        String query = String.format(MySqlSamples.UPDATE.QUERY, TABLE, sets,"id = ?");
        try (Request request = new Request(query)) {
            byte i = 1;
            WorkRecord workRecord = (WorkRecord) object;
            PreparedStatement statement = request.getPreparedStatement();
            statement.setString(i++, workRecord.getPatient());
            statement.setString(i++, workRecord.getClinic());
            statement.setDate(i++, Date.valueOf(workRecord.getAccepted()));
            if (workRecord.getComplete() != null) {
                statement.setDate(i++, Date.valueOf(workRecord.getComplete()));
            } else {
                statement.setNull(i++, Types.DATE);
            }
            statement.setString(i++, String.valueOf(workRecord.getStatus()));
            if (workRecord.getPhoto() != null) {
                Blob photo = request.createBlob();
                photo.setBytes(1, workRecord.getPhoto());
                statement.setBlob(i++, photo);
            } else {
                statement.setNull(i++, Types.BLOB);
            }
            statement.setString(i++, workRecord.getComment());
            if (workRecord.getReportId() > 0) {
                statement.setInt(i, workRecord.getReportId());
            } else {
                statement.setNull(i, Types.INTEGER);
            }
            return new ProductMySql(workRecord.getId()).overwrite(workRecord.getProducts())
                    && statement.execute();
        } catch (SQLException | ClassCastException e) {
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
            return new ProductMySql(workId).deleteAll();
        } catch (SQLException e) {
            //TODO loggers
            throw new DatabaseException(e.getMessage(), e.getCause());
        }
    }

    private String buildQuery(WorkRecord wr) {
        String values = "DEFAULT, '%s', '%s', '%s', '%s', '%s', %s, '%s', %s";
        values = String.format(values, wr.getPatient(), wr.getClinic(), wr.getAccepted(),
                wr.getComplete(), wr.getStatus(), Arrays.toString(wr.getPhoto()), wr.getComment(), wr.getReportId());
        return String.format(MySqlSamples.INSERT_BATCH.QUERY, TABLE, values);
    }


    protected static class WorkRecordInstantiation implements Instantiating<I_WorkRecord> {

        private final MyList<I_WorkRecord> recordsList;
        private final ResultSet resultSet;

        public WorkRecordInstantiation(ResultSet resultSet) {
            this.resultSet = resultSet;
            this.recordsList = new MyList<>();
        }

        @Override
        public Collection<I_WorkRecord> build() throws SQLException, IOException, DatabaseException {
            try (resultSet) {
                String[] fields = FIELDS.split(", ");
                while (resultSet.next()) {
                    byte i = 0;
                    WorkRecord workRecord = new WorkRecord();
                    workRecord.setId(resultSet.getInt(fields[i++]));
                    workRecord.setPatient(resultSet.getString(fields[i++]));
                    workRecord.setClinic(resultSet.getString(fields[i++]));
                    workRecord.setAccepted(resultSet.getDate(fields[i++]).toLocalDate());
                    Date complete = resultSet.getDate(fields[i++]);
                    if (complete != null) {
                        workRecord.setComplete(complete.toLocalDate());
                    }
                    String s = resultSet.getString(fields[i++]);
                    WorkRecord.Status status = Enum.valueOf(WorkRecord.Status.class, s);
                    workRecord.setStatus(status);
                    Blob blob = resultSet.getBlob(fields[i++]);
                    if (blob != null) {
                        workRecord.setPhoto(blob.getBytes(1, (int) blob.length()));
                        blob.free();
                    }
                    workRecord.setComment(resultSet.getString(fields[i]));
                    MyList<Product> products = (MyList<Product>)
                            new ProductMySql(workRecord.getId()).instantiate(resultSet);
                    workRecord.setProducts(products);
                    recordsList.add(workRecord);
                }
            }
            return this.recordsList;
        }
    }
}
