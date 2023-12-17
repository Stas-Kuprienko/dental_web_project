package edu.dental.database.mysql_api;

import edu.dental.database.DatabaseException;
import edu.dental.database.DatabaseService;
import edu.dental.database.TableInitializer;
import edu.dental.database.dao.DAO;
import edu.dental.database.dao.DentalWorkDAO;
import edu.dental.domain.APIManager;
import edu.dental.domain.entities.DentalWork;
import edu.dental.domain.entities.Product;
import edu.dental.domain.entities.User;
import edu.dental.domain.records.WorkRecordBook;
import edu.dental.domain.utils.DatesTool;
import utils.collections.SimpleList;

import java.io.IOException;
import java.sql.*;
import java.util.List;

public class DentalWorkMySql implements DentalWorkDAO {

    public final String TABLE = TableInitializer.DENTAL_WORK;

    private static final String FIELDS =
            "id, user_id, report_id, patient, clinic, accepted, complete, status, comment, user_id";

    DentalWorkMySql() {}


    @Override
    public boolean putAll(List<DentalWork> list) throws DatabaseException {
        if (list == null || list.isEmpty()) {
            throw new DatabaseException("The given argument is null or empty.");
        }
        try (Request request = new Request()){
            Statement statement = request.getStatement();
            for (DentalWork dw : list) {
                String query = buildUpdateQuery(dw);
                statement.addBatch(query);
            }
            return statement.executeBatch().length == list.size();
        } catch (SQLException e) {
            //TODO loggers
            throw new DatabaseException(e.getMessage(), e.getCause());
        }
    }

    @Override
    public boolean put(DentalWork object) throws DatabaseException {
        String injections = "?, ".repeat(FIELDS.split(", ").length - 2);
        injections = "DEFAULT, ".repeat(2) + injections.substring(0, injections.length() - 2);
        String query = String.format(MySqlSamples.INSERT.QUERY, TABLE, FIELDS, injections);
        try (Request request = new Request(query)) {
            byte i = 1;
            PreparedStatement statement = request.getPreparedStatement();
            statement.setString(i++, object.getPatient());
            statement.setString(i++, object.getClinic());
            statement.setDate(i++, Date.valueOf(object.getAccepted()));
            if (object.getComplete() == null) {
                statement.setNull(i++, Types.DATE);
            } else {
                statement.setDate(i++, Date.valueOf(object.getComplete()));
            }            statement.setString(i++, String.valueOf(object.getStatus()));
            statement.setString(i++, object.getComment());
            statement.setInt(i, object.getUserId());
            statement.executeUpdate();
            boolean result = request.setID(object);
            if (object.getProducts().isEmpty()) {
                return result;
            }
            return new ProductMySql(object.getId()).putAll(object.getProducts());
        } catch (SQLException e) {
            //TODO loggers
            throw new DatabaseException(e.getMessage(), e.getCause());
        }
    }

    @Override
    public List<DentalWork> getAll(int userId) throws DatabaseException {
        String where = String.format("%s.user_id = %s AND %s.report_id IS NULL",
                TableInitializer.DENTAL_WORK, userId, TableInitializer.DENTAL_WORK);
        String query = String.format(MySqlSamples.SELECT_DENTAL_WORK.QUERY, where);
        SimpleList<DentalWork> dentalWorks;
        try (Request request = new Request(query)) {
            ResultSet resultSet = request.getPreparedStatement().executeQuery();
            dentalWorks = (SimpleList<DentalWork>) new DentalWorkInstantiation(resultSet).build();
        } catch (SQLException | IOException | ClassCastException e) {
            //TODO loggers
            throw new DatabaseException(e.getMessage(), e.getCause());
        }
        return dentalWorks;
    }

    @Override
    public List<DentalWork> getAllMonthly(int userId, String month, String year) throws DatabaseException {
        String getReportId = "(SELECT id FROM " + TableInitializer.REPORT +" WHERE month = ? AND year = ?)";
        String where = TableInitializer.DENTAL_WORK + ".user_id = " + userId + " AND report_id = " + getReportId;
        String query = String.format(MySqlSamples.SELECT_DENTAL_WORK.QUERY, where);
        SimpleList<DentalWork> dentalWorks;
        try (Request request = new Request(query)) {
            PreparedStatement statement = request.getPreparedStatement();
            statement.setString(1, month);
            statement.setInt(2, Integer.parseInt(year));
            ResultSet resultSet = statement.executeQuery();
            dentalWorks = (SimpleList<DentalWork>) new DentalWorkInstantiation(resultSet).build();
        } catch (SQLException | IOException | ClassCastException e) {
            //TODO loggers
            throw new DatabaseException(e.getMessage(), e.getCause());
        }
        return dentalWorks;
    }

    @Override
    public DentalWork get(int id) throws DatabaseException {
        String query = String.format(MySqlSamples.SELECT_WHERE.QUERY, "*", TABLE, "id = ?");
        try (Request request = new Request(query)) {
            request.getPreparedStatement().setInt(1, id);
            ResultSet resultSet = request.getPreparedStatement().executeQuery();
            SimpleList<DentalWork> list = (SimpleList<DentalWork>) new DentalWorkInstantiation(resultSet).build();
            return list.get(0);
        } catch (SQLException | IOException | NullPointerException e) {
            //TODO loggers
            throw new DatabaseException(e.getMessage(), e.getCause());
        }
    }

    @Override
    public SimpleList<DentalWork> search(Object... args) throws DatabaseException {
        String where = "patient = ? AND clinic = ?";
        String query = String.format(MySqlSamples.SELECT_WHERE.QUERY, "*", TABLE, where);
        try (Request request = new Request(query)) {
            request.getPreparedStatement().setString(1, (String) args[0]);
            request.getPreparedStatement().setString(2, (String) args[1]);
            ResultSet resultSet = request.getPreparedStatement().executeQuery();
            return (SimpleList<DentalWork>) new DentalWorkInstantiation(resultSet).build();
        } catch (SQLException | IOException | NullPointerException | ClassCastException e) {
            //TODO loggers
            throw new DatabaseException(e.getMessage(), e.getCause());
        }
    }

    @Override
    public boolean edit(DentalWork object) throws DatabaseException {
        StringBuilder sets = new StringBuilder();
        String[] fields = FIELDS.split(", ");
        for (int i = 1; i < fields.length - 1; i++) {
            sets.append(fields[i]).append("=?,");
        } sets.deleteCharAt(sets.length()-1);
        String query = String.format(MySqlSamples.UPDATE.QUERY, TABLE, sets,"id = ? AND user_id = ?");
        try (Request request = new Request(query)) {
            byte i = 1;
            PreparedStatement statement = request.getPreparedStatement();
            if (object.getReportId() > 0) {
                statement.setInt(i++, object.getReportId());
            } else {
                statement.setNull(i++, Types.INTEGER);
            }
            statement.setString(i++, object.getPatient());
            statement.setString(i++, object.getClinic());
            statement.setDate(i++, Date.valueOf(object.getAccepted()));
            if (object.getComplete() != null) {
                statement.setDate(i++, Date.valueOf(object.getComplete()));
            } else {
                statement.setNull(i++, Types.DATE);
            }
            statement.setString(i++, String.valueOf(object.getStatus()));
            statement.setString(i++, object.getComment());
            statement.setInt(i++, object.getId());
            statement.setInt(i, object.getUserId());
            return new ProductMySql(object.getId()).overwrite(object.getProducts())
                    && statement.executeUpdate() > 0;
        } catch (SQLException | ClassCastException e) {
            //TODO loggers
            throw new DatabaseException(e.getMessage(), e.getCause());
        }
    }

    @Override
    public boolean setFieldValue(List<DentalWork> list, String field, Object value) throws DatabaseException {
        if (list == null || list.isEmpty()) {
            return false;
        }
        String set = field + " = ?";
        String where = "user_id = " + list.get(0).getUserId() + " AND id = ?";
        String query = String.format(MySqlSamples.UPDATE.QUERY, TABLE, set, where);
        try (Request request = new Request(query)){
            PreparedStatement statement = request.getPreparedStatement();
            for (DentalWork dw : list) {
                statement.setObject(1, String.valueOf(value));
                statement.setInt(2, dw.getId());
                statement.addBatch();
            }
            return statement.executeBatch().length == list.size();
        } catch (SQLException e) {
            //TODO loggers
            throw new DatabaseException(e.getMessage(), e.fillInStackTrace());
        }
    }

    @Override
    public int setReportId(List<DentalWork> list) throws DatabaseException {
        if (list == null || list.isEmpty()) {
            return 0;
        }
        String[] yearAndMonth = DatesTool.getYearAndMonth(WorkRecordBook.PAY_DAY);
        String getReportId = String.format(MySqlSamples.REPORT_ID.QUERY, yearAndMonth[0], yearAndMonth[1]);
        String query = String.format((MySqlSamples.UPDATE.QUERY), TABLE, "report_id = (" + getReportId + ')', "id = ?");
        try (Request request = new Request(query)) {
            PreparedStatement statement = request.getPreparedStatement();
            for (DentalWork dw : list) {
                statement.setInt(1, dw.getId());
                statement.addBatch();
            }
            int executed = statement.executeBatch().length;
            if (!(executed == list.size())) {
                throw new DatabaseException("Error of batch executing. Finally result - " + executed
                + ", the given list size - " + list.size());
            }
            statement.clearParameters();
            ResultSet resultSet = statement.executeQuery(getReportId);
            resultSet.next();
            return resultSet.getInt("id");
        } catch (SQLException e) {
            if (e.getMessage().equals("Illegal operation on empty result set.")) {
                DatabaseService.getInstance().getTableInitializer().addReports();
                return setReportId(list);
            } else {
                //TODO loggers
                throw new DatabaseException(e.getMessage(), e.fillInStackTrace());
            }
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
        String values = "DEFAULT, '%s', '%s', '%s', '%s', '%s', '%s', %s";
        values = String.format(values, dw.getPatient(), dw.getClinic(), dw.getAccepted(),
                dw.getComplete(), dw.getStatus(), dw.getComment(), dw.getReportId());
        return String.format(MySqlSamples.INSERT_BATCH.QUERY, TABLE, values);
    }


    protected static class DentalWorkInstantiation implements DAO.Instantiating<DentalWork> {

        private final SimpleList<DentalWork> recordsList;
        private final ResultSet resultSet;

        public DentalWorkInstantiation(ResultSet resultSet) {
            this.resultSet = resultSet;
            this.recordsList = new SimpleList<>();
        }

        @Override
        public List<DentalWork> build() throws SQLException, IOException, DatabaseException {
            try (resultSet) {
                String[] fields = FIELDS.split(", ");
                while (resultSet.next()) {
                    byte i = 0;
                    DentalWork dentalWork = new DentalWork();
                    dentalWork.setId(resultSet.getInt(fields[i++]));
                    dentalWork.setUserId(resultSet.getInt(fields[i++]));
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
                    dentalWork.setComment(resultSet.getString(fields[i]));
                    SimpleList<Product> products = (SimpleList<Product>)
                            new ProductMySql(dentalWork.getId()).instantiate(resultSet);
                    dentalWork.setProducts(products);
                    recordsList.add(dentalWork);
                }
            }
            return this.recordsList;
        }
    }
}
