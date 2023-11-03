package edu.dental.database.mysql_api.dao;

import edu.dental.database.DatabaseException;
import edu.dental.database.TableInitializer;
import edu.dental.database.dao.DentalWorkDAO;
import edu.dental.domain.entities.DentalWork;
import edu.dental.domain.entities.I_DentalWork;
import edu.dental.domain.entities.Product;
import edu.dental.domain.entities.User;
import edu.dental.domain.records.WorkRecordBook;
import edu.dental.utils.DatesTool;
import edu.dental.utils.data_structures.MyList;

import java.io.IOException;
import java.lang.reflect.Field;
import java.sql.*;
import java.util.Arrays;
import java.util.Collection;

public class DentalWorkMySql implements DentalWorkDAO {

    public final String TABLE = TableInitializer.DENTAL_WORK;
    private final User user;

    private static final String FIELDS = "id, report_id, patient, clinic, accepted, complete, status, photo, comment, user_id";

    public DentalWorkMySql(User user) {
        this.user = user;
    }


    @Override
    public boolean putAll(Collection<I_DentalWork> list) throws DatabaseException {
        if (list == null || list.isEmpty()) {
            throw new DatabaseException("The given argument is null or empty.");
        }
        try (Request request = new Request()){
            Statement statement = request.getStatement();
            for (I_DentalWork dw : list) {
                String query = buildUpdateQuery((DentalWork) dw);
                statement.addBatch(query);
            }
            return statement.executeBatch().length == list.size();
        } catch (SQLException e) {
            throw new DatabaseException(e.getMessage(), e.getCause());
        }
    }

    @Override
    public boolean put(I_DentalWork object) throws DatabaseException {
        String injections = "?, ".repeat(FIELDS.split(", ").length - 2);
        injections = "DEFAULT, ".repeat(2) + injections.substring(0, injections.length() - 2);
        String query = String.format(MySqlSamples.INSERT.QUERY, TABLE, FIELDS, injections);
        try (Request request = new Request(query)) {
            byte i = 1;
            DentalWork dentalWork = (DentalWork) object;
            PreparedStatement statement = request.getPreparedStatement();
            statement.setString(i++, dentalWork.getPatient());
            statement.setString(i++, dentalWork.getClinic());
            statement.setDate(i++, Date.valueOf(dentalWork.getAccepted()));
            statement.setDate(i++, Date.valueOf(dentalWork.getComplete()));
            statement.setString(i++, String.valueOf(dentalWork.getStatus()));
            Blob photo = request.createBlob();
            if (dentalWork.getPhoto() != null) {
                photo.setBytes(1, dentalWork.getPhoto());
                statement.setBlob(i++, photo);
            } else {
                statement.setNull(i++, Types.BLOB);
            }
            statement.setString(i++, dentalWork.getComment());
            statement.setInt(i, user.getId());
            statement.executeUpdate();
            photo.free();
            request.setID(dentalWork);
            return new ProductMySql(dentalWork.getId()).putAll(dentalWork.getProducts());
        } catch (SQLException e) {
            //TODO loggers
            throw new DatabaseException(e.getMessage(), e.getCause());
        }
    }

    @Override
    public Collection<I_DentalWork> getAll() throws DatabaseException {
        String where = String.format("%s.user_id = %s AND %s.report_id IS NULL",
                TableInitializer.DENTAL_WORK, user.getId(), TableInitializer.DENTAL_WORK);
        String query = String.format(MySqlSamples.SELECT_DENTAL_WORK.QUERY, where);
        MyList<I_DentalWork> dentalWorks;
        try (Request request = new Request(query)) {
            ResultSet resultSet = request.getPreparedStatement().executeQuery();
            dentalWorks = (MyList<I_DentalWork>) new DentalWorkInstantiation(resultSet).build();
        } catch (SQLException | IOException | ClassCastException e) {
            //TODO loggers
            throw new DatabaseException(e.getMessage(), e.getCause());
        }
        return dentalWorks;
    }

    @Override
    public Collection<I_DentalWork> getAllMonthly(String month, String year) throws DatabaseException {
        String getReportId = "(SELECT id FROM " + TableInitializer.REPORT +" WHERE month = ? AND year = ?)";
        String where = TableInitializer.DENTAL_WORK + ".user_id = " + user.getId() + " AND report_id = " + getReportId;
        String query = String.format(MySqlSamples.SELECT_DENTAL_WORK.QUERY, where);
        MyList<I_DentalWork> dentalWorks;
        try (Request request = new Request(query)) {
            PreparedStatement statement = request.getPreparedStatement();
            statement.setString(1, month);
            statement.setInt(2, Integer.parseInt(year));
            ResultSet resultSet = statement.executeQuery();
            dentalWorks = (MyList<I_DentalWork>) new DentalWorkInstantiation(resultSet).build();
        } catch (SQLException | IOException | ClassCastException e) {
            //TODO loggers
            throw new DatabaseException(e.getMessage(), e.getCause());
        }
        return dentalWorks;
    }

    @Override
    public I_DentalWork get(int id) throws DatabaseException {
        String query = String.format(MySqlSamples.SELECT_WHERE.QUERY, "*", TABLE, "id = ?");
        try (Request request = new Request(query)) {
            request.getPreparedStatement().setInt(1, id);
            ResultSet resultSet = request.getPreparedStatement().executeQuery();
            MyList<I_DentalWork> list = (MyList<I_DentalWork>) new DentalWorkInstantiation(resultSet).build();
            return list.get(0);
        } catch (SQLException | IOException | NullPointerException e) {
            //TODO loggers
            throw new DatabaseException(e.getMessage(), e.getCause());
        }
    }

    @Override
    public MyList<I_DentalWork> search(Object... args) throws DatabaseException {
        String where = "patient = ? AND clinic = ?";
        String query = String.format(MySqlSamples.SELECT_WHERE.QUERY, "*", TABLE, where);
        try (Request request = new Request(query)) {
            request.getPreparedStatement().setString(1, (String) args[0]);
            request.getPreparedStatement().setString(2, (String) args[1]);
            ResultSet resultSet = request.getPreparedStatement().executeQuery();
            return (MyList<I_DentalWork>) new DentalWorkInstantiation(resultSet).build();
        } catch (SQLException | IOException | NullPointerException | ClassCastException e) {
            //TODO loggers
            throw new DatabaseException(e.getMessage(), e.getCause());
        }
    }

    @Override
    public boolean edit(I_DentalWork object) throws DatabaseException {
        StringBuilder sets = new StringBuilder();
        String[] fields = FIELDS.split(", ");
        for (int i = 1; i < fields.length - 1; i++) {
            sets.append(fields[i]).append("=?,");
        } sets.deleteCharAt(sets.length()-1);
        String query = String.format(MySqlSamples.UPDATE.QUERY, TABLE, sets,"id = ? AND user_id = ?");
        try (Request request = new Request(query)) {
            byte i = 1;
            DentalWork dentalWork = (DentalWork) object;
            PreparedStatement statement = request.getPreparedStatement();
            if (dentalWork.getReportId() > 0) {
                statement.setInt(i++, dentalWork.getReportId());
            } else {
                statement.setNull(i++, Types.INTEGER);
            }
            statement.setString(i++, dentalWork.getPatient());
            statement.setString(i++, dentalWork.getClinic());
            statement.setDate(i++, Date.valueOf(dentalWork.getAccepted()));
            if (dentalWork.getComplete() != null) {
                statement.setDate(i++, Date.valueOf(dentalWork.getComplete()));
            } else {
                statement.setNull(i++, Types.DATE);
            }
            statement.setString(i++, String.valueOf(dentalWork.getStatus()));
            if (dentalWork.getPhoto() != null) {
                Blob photo = request.createBlob();
                photo.setBytes(1, dentalWork.getPhoto());
                statement.setBlob(i++, photo);
            } else {
                statement.setNull(i++, Types.BLOB);
            }
            statement.setString(i++, dentalWork.getComment());
            statement.setInt(i++, dentalWork.getId());
            statement.setInt(i, user.getId());
            return new ProductMySql(dentalWork.getId()).overwrite(dentalWork.getProducts())
                    && statement.executeUpdate() > 0;
        } catch (SQLException | ClassCastException e) {
            //TODO loggers
            throw new DatabaseException(e.getMessage(), e.getCause());
        }
    }

    @Override
    public boolean setFieldValue(Collection<I_DentalWork> list, String field, Object value) throws DatabaseException {
        if (list == null || list.isEmpty()) {
            throw new DatabaseException("The given argument is null or empty.");
        }
        String set = field + " = ?";
        String where = "user_id = " + user.getId() + " AND id = ?";
        String query = String.format(MySqlSamples.UPDATE.QUERY, TABLE, set, where);
        try (Request request = new Request(query)){
            PreparedStatement statement = request.getPreparedStatement();
            for (I_DentalWork dw : list) {
                statement.setObject(1, String.valueOf(value));
                statement.setInt(2, dw.getId());
                statement.addBatch();
            }
            return statement.executeBatch().length == list.size();
        } catch (SQLException e) {
            throw new DatabaseException(e.getMessage(), e.fillInStackTrace());
        }
    }

    public int setReportId(Collection<I_DentalWork> list) throws DatabaseException {
        if (list == null || list.isEmpty()) {
            throw new DatabaseException("The given argument is null or empty.");
        }
        String[] yearAndMonth = DatesTool.getYearAndMonth(WorkRecordBook.PAY_DAY);
        String getReportId = String.format(MySqlSamples.REPORT_ID.QUERY, yearAndMonth[0], yearAndMonth[1]);
        String query = String.format((MySqlSamples.UPDATE.QUERY), TABLE, "report_id = (" + getReportId + ')', "id = ?");
        try (Request request = new Request(query)) {
            PreparedStatement statement = request.getPreparedStatement();
            for (I_DentalWork dw : list) {
                statement.setInt(1, dw.getId());
                statement.addBatch();
            }
            int executed = statement.executeBatch().length;
            if (!(executed == list.size())) {
                throw new DatabaseException("error of batch executing. Finally result - " + executed);
            }
            statement.clearParameters();
            ResultSet resultSet = statement.executeQuery(getReportId);
            resultSet.next();
            return resultSet.getInt("id");
        } catch (SQLException e) {
            throw new DatabaseException(e.getMessage(), e.fillInStackTrace());
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

    private String buildUpdateQuery(DentalWork dw) {
        String values = "DEFAULT, '%s', '%s', '%s', '%s', '%s', %s, '%s', %s";
        values = String.format(values, dw.getPatient(), dw.getClinic(), dw.getAccepted(),
                dw.getComplete(), dw.getStatus(), Arrays.toString(dw.getPhoto()), dw.getComment(), dw.getReportId());
        return String.format(MySqlSamples.INSERT_BATCH.QUERY, TABLE, values);
    }


    protected static class DentalWorkInstantiation implements Instantiating<I_DentalWork> {

        private final MyList<I_DentalWork> recordsList;
        private final ResultSet resultSet;

        public DentalWorkInstantiation(ResultSet resultSet) {
            this.resultSet = resultSet;
            this.recordsList = new MyList<>();
        }

        @Override
        public Collection<I_DentalWork> build() throws SQLException, IOException, DatabaseException {
            try (resultSet) {
                String[] fields = FIELDS.split(", ");
                while (resultSet.next()) {
                    byte i = 0;
                    DentalWork dentalWork = new DentalWork();
                    dentalWork.setId(resultSet.getInt(fields[i++]));
                    dentalWork.setReportId(resultSet.getInt(fields[i++]));
                    dentalWork.setPatient(resultSet.getString(fields[i++]));
                    dentalWork.setClinic(resultSet.getString(fields[i++]));
                    dentalWork.setAccepted(resultSet.getDate(fields[i++]).toLocalDate());
                    Date complete = resultSet.getDate(fields[i++]);
                    if (complete != null) {
                        dentalWork.setComplete(complete.toLocalDate());
                    }
                    String s = resultSet.getString(fields[i++]);
                    DentalWork.Status status = Enum.valueOf(DentalWork.Status.class, s);
                    dentalWork.setStatus(status);
                    Blob blob = resultSet.getBlob(fields[i++]);
                    if (blob != null) {
                        dentalWork.setPhoto(blob.getBytes(1, (int) blob.length()));
                        blob.free();
                    }
                    dentalWork.setComment(resultSet.getString(fields[i]));
                    MyList<Product> products = (MyList<Product>)
                            new ProductMySql(dentalWork.getId()).instantiate(resultSet);
                    dentalWork.setProducts(products);
                    recordsList.add(dentalWork);
                }
            }
            return this.recordsList;
        }
    }
}
