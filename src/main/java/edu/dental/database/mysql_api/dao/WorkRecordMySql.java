package edu.dental.database.mysql_api.dao;

import edu.dental.database.DatabaseException;
import edu.dental.database.connection.DBConfiguration;
import edu.dental.database.interfaces.DAO;
import edu.dental.domain.entities.User;
import edu.dental.domain.entities.WorkRecord;
import edu.dental.utils.data_structures.MyList;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.*;
import java.util.Collection;

public class WorkRecordMySql implements DAO<WorkRecord> {

    public static final String TABLE = DBConfiguration.DATA_BASE + ".work_record_";

    private static final String FIELDS = "id, patient, clinic, accepted, complete, closed, paid, photo, comment";

    private static final String PHOTO_FORMAT = "png";

    private final User user;

    public WorkRecordMySql(User user) {
        this.user = user;
    }

    @Override
    public boolean put(WorkRecord object) throws DatabaseException {
        String injections = "?, ".repeat(FIELDS.split(", ").length - 1);
        injections = "DEFAULT, " + injections.substring(0, injections.length() - 2);
        String query = String.format(MySqlSamples.INSERT.QUERY, TABLE + user.getId(), FIELDS, injections);
        try (Request request = new Request(query)) {
            byte i = 2;
            PreparedStatement statement = request.getStatement();
            statement.setString(i++, object.getPatient());
            statement.setString(i++, object.getClinic());
            statement.setDate(i++, Date.valueOf(object.getAccepted()));
            statement.setDate(i++, Date.valueOf(object.getComplete()));
            statement.setBoolean(i++, object.isClosed());
            statement.setBoolean(i++, object.isPaid());
            Blob photoBlob = request.createBlob();
            OutputStream out = photoBlob.setBinaryStream(1);
            ImageIO.write(object.getPhoto(), PHOTO_FORMAT, out);
            statement.setBlob(i++, photoBlob);
            out.close();
            statement.setString(i, object.getComment());
            //TODO products
            statement.executeUpdate();
            return request.setID(object);
        } catch (SQLException | IOException e) {
            //TODO loggers
            throw new DatabaseException(e.getMessage(), e.getCause());
        }
    }

    @Override
    public Collection<WorkRecord> getAll() throws DatabaseException {
        String query = String.format(MySqlSamples.SELECT_ALL.QUERY, "*", TABLE + user.getId());
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
        String query = String.format(MySqlSamples.SELECT_WHERE.QUERY, "*", TABLE + user.getId(), "id = ?");
        ResultSet resultSet;
        try (Request request = new Request(query)) {
            request.getStatement().setInt(1, id);
            resultSet = request.getStatement().executeQuery();
            MyList<WorkRecord> list = (MyList<WorkRecord>) new WorkRecordInstantiation(resultSet).build();
            return list.get(0);
        } catch (SQLException | IOException | NullPointerException e) {
            //TODO loggers
            throw new DatabaseException(e.getMessage(), e.getCause());
        }
    }

    @Override
    public WorkRecord search(Object value1, Object value2) throws DatabaseException {
        String where = "patient = ? AND clinic = ?";
        String query = String.format(MySqlSamples.SELECT_WHERE.QUERY, "*", TABLE + user.getId(), where);
        ResultSet resultSet;
        try (Request request = new Request(query)) {
            request.getStatement().setString(1, (String) value1);
            request.getStatement().setString(2, (String) value2);
            resultSet = request.getStatement().executeQuery();
            MyList<WorkRecord> list = (MyList<WorkRecord>) new WorkRecordInstantiation(resultSet).build();
            return list.get(0);
        } catch (SQLException | IOException | NullPointerException | ClassCastException e) {
            //TODO loggers
            throw new DatabaseException(e.getMessage(), e.getCause());
        }
    }

    @Override
    public boolean edit(WorkRecord object, Object... args) {
        String query;
        return false;
    }

    @Override
    public boolean delete(int id) throws DatabaseException {
        String query = String.format(MySqlSamples.DELETE.QUERY, TABLE + user.getId(), "id = ?");
        try (Request request = new Request(query)) {
            request.getStatement().setInt(1, id);
            return request.getStatement().execute();
        } catch (SQLException e) {
            //TODO loggers
            throw new DatabaseException(e.getMessage(), e.getCause());
        }
    }


    protected static class WorkRecordInstantiation implements Instantiating<WorkRecord> {

        private final MyList<WorkRecord> recordsList;
        private final ResultSet resultSet;

        public WorkRecordInstantiation(ResultSet resultSet) {
            this.resultSet = resultSet;
            this.recordsList = new MyList<>();
        }

        @Override
        public Collection<WorkRecord> build() throws SQLException, IOException {
            try (resultSet) {
                String[] fields = FIELDS.split(", ");
                while (resultSet.next()) {
                    byte i = 0;
                    WorkRecord workRecord = new WorkRecord();
                    workRecord.setId(resultSet.getInt(fields[i++]));
                    workRecord.setPatient(resultSet.getString(i++));
                    workRecord.setClinic(resultSet.getString(i++));
                    workRecord.setAccepted(resultSet.getDate(i++).toLocalDate());
                    workRecord.setComplete(resultSet.getDate(i++).toLocalDate());
                    workRecord.setClosed(resultSet.getBoolean(i++));
                    workRecord.setPaid(resultSet.getBoolean(i++));
                    Blob blob = resultSet.getBlob(i++);
                    BufferedImage photo = ImageIO.read(blob.getBinaryStream());
                    workRecord.setPhoto(photo);
                    workRecord.setComment(resultSet.getString(i));
                    //TODO products
                    recordsList.add(workRecord);
                }
            }
            return this.recordsList;
        }
    }
}
