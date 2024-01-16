package edu.dental.database.mysql_api;

import edu.dental.database.DatabaseException;
import edu.dental.database.DatabaseService;
import edu.dental.database.TableInitializer;
import edu.dental.database.dao.DAO;
import edu.dental.database.dao.DentalWorkDAO;
import edu.dental.entities.DentalWork;
import edu.dental.entities.Product;
import utils.collections.SimpleList;

import java.io.IOException;
import java.sql.*;
import java.util.List;

public class DentalWorkMySql implements DentalWorkDAO, MySQL_DAO {

    public final String TABLE = TableInitializer.DENTAL_WORK;

    private static final String FIELDS =
            "id, report_id, patient, clinic, accepted, complete, status, comment, user_id";

    DentalWorkMySql() {}


    @Override
    public boolean putAll(List<DentalWork> list) throws DatabaseException {
        if (list == null || list.isEmpty()) {
            throw new DatabaseException(new NullPointerException("The given argument is null or empty."));
        }
        try (Request request = new Request()){
            Statement statement = request.getStatement();
            for (DentalWork dw : list) {
                String query = buildInsertQuery(dw);
                statement.addBatch(query);
            }
            return statement.executeBatch().length == list.size();
        } catch (SQLException e) {
            throw new DatabaseException(e);
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
            throw new DatabaseException(e);
        }
    }

    @Override
    public List<DentalWork> getAll(int userId) throws DatabaseException {
        String where = String.format("%s.user_id = %s AND %s.report_id IS NULL",
                TABLE, userId, TABLE);
        String query = String.format(MySqlSamples.SELECT_DENTAL_WORK.QUERY, where);
        SimpleList<DentalWork> dentalWorks;
        try (Request request = new Request(query)) {
            ResultSet resultSet = request.getPreparedStatement().executeQuery();
            dentalWorks = (SimpleList<DentalWork>) new DentalWorkInstantiation(resultSet).build();
        } catch (SQLException | IOException | ClassCastException e) {
            throw new DatabaseException(e);
        }
        return dentalWorks;
    }

    @Override
    public List<DentalWork> getAllMonthly(int userId, String month, int year) throws DatabaseException {
        String getReportId = "(SELECT id FROM " + TableInitializer.REPORT +" WHERE month = ? AND year = ?)";
        String where = TABLE + ".user_id = " + userId + " AND report_id = " + getReportId;
        String query = String.format(MySqlSamples.SELECT_DENTAL_WORK.QUERY, where);
        SimpleList<DentalWork> dentalWorks;
        try (Request request = new Request(query)) {
            PreparedStatement statement = request.getPreparedStatement();
            statement.setString(1, month);
            statement.setInt(2, year);
            ResultSet resultSet = statement.executeQuery();
            dentalWorks = (SimpleList<DentalWork>) new DentalWorkInstantiation(resultSet).build();
        } catch (SQLException | IOException | ClassCastException e) {
            throw new DatabaseException(e);
        }
        return dentalWorks;
    }

    @Override
    public DentalWork get(int userId, int id) throws DatabaseException {
        String where = TABLE + ".id = ? AND " + TABLE + ".user_id = ?";
        String query = String.format(MySqlSamples.SELECT_DENTAL_WORK.QUERY, where);
        try (Request request = new Request(query)) {
            PreparedStatement statement = request.getPreparedStatement();
            statement.setInt(1, id);
            statement.setInt(2, userId);
            ResultSet resultSet = statement.executeQuery();
            SimpleList<DentalWork> list = (SimpleList<DentalWork>) new DentalWorkInstantiation(resultSet).build();
            return list.get(0);
        } catch (SQLException | IOException | NullPointerException e) {
            throw new DatabaseException(e);
        }
    }

    @Override
    public SimpleList<DentalWork> search(int userId, String[] fields, String[] args) throws DatabaseException {
        String where = buildSearchQuery(fields, args);
        if (where == null) {
            throw new DatabaseException(new IllegalArgumentException("Incorrect search parameters"));
        }
        where += TABLE + ".user_id = " + userId;
        String query = String.format(MySqlSamples.SELECT_DENTAL_WORK.QUERY, where);
        try (Request request = new Request()) {
            ResultSet resultSet = request.getStatement().executeQuery(query);
            return (SimpleList<DentalWork>) new DentalWorkInstantiation(resultSet).build();
        } catch (SQLException | IOException | NullPointerException | ClassCastException e) {
            throw new DatabaseException(e);
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
            throw new DatabaseException(e);
        }
    }

    @Override
    public boolean setFieldValue(int userId, List<DentalWork> list, String field, Object value) throws DatabaseException {
        if (list == null || list.isEmpty()) {
            return false;
        }
        String set = field + " = ?";
        String where = "user_id = " + userId + " AND id = ?";
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
            throw new DatabaseException(e);
        }
    }

    @Override
    public int setReportId(int userId, List<DentalWork> list, String month, String year) throws DatabaseException {
        if (list == null || list.isEmpty()) {
            return 0;
        }
        int reportId = getReportId(month, year);
        String set = "report_id = " + reportId;
        String where = "user_id = " + userId + " AND id = ?";
        String query = String.format((MySqlSamples.UPDATE.QUERY), TABLE, set, where);
        try (Request request = new Request(query)) {
            PreparedStatement statement = request.getPreparedStatement();
            for (DentalWork dw : list) {
                statement.setInt(1, dw.getId());
                statement.addBatch();
            }
            int executed = statement.executeBatch().length;
            if (!(executed == list.size())) {
                throw new DatabaseException(new SQLException("Error of batch executing. Finally result - "
                        + executed + ", the given list size - " + list.size()));
            }
            return reportId;
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    @Override
    public int getReportId(String month, String year) throws DatabaseException {
        String query = String.format(MySqlSamples.REPORT_ID.QUERY);
        try (Request request = new Request(query)) {
            PreparedStatement statement = request.getPreparedStatement();
            statement.setInt(1, Integer.parseInt(year));
            statement.setString(2, month);
            ResultSet resultSet = statement.executeQuery();
            resultSet.next();
            return resultSet.getInt("id");
        } catch (SQLException e) {
            if (e.getMessage().equals("Illegal operation on empty result set.")) {
                DatabaseService.getInstance().getTableInitializer().addReports();
                return getReportId(month, year);
            } else {
                throw new DatabaseException(e);
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
            throw new DatabaseException(e);
        }
    }


    private String buildInsertQuery(DentalWork dw) {
        String values = "DEFAULT, '%s', '%s', '%s', '%s', '%s', '%s', %s";
        values = String.format(values, dw.getPatient(), dw.getClinic(), dw.getAccepted(),
                dw.getComplete(), dw.getStatus(), dw.getComment(), dw.getReportId());
        return String.format(MySqlSamples.INSERT_BATCH.QUERY, TABLE, values);
    }

    private String buildSearchQuery(String[] fields, String[] args) {
        if (fields.length != args.length) {
            return null;
        }
        String queryFormat = TABLE + ".%s = '%s' AND ";
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < fields.length; i++) {
            result.append(String.format(queryFormat, fields[i], args[i]));
        }
        return result.toString();
    }


    protected static class DentalWorkInstantiation implements DAO.Instantiation<DentalWork> {

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
                    dentalWork.setComment(resultSet.getString(fields[i++]));
                    dentalWork.setUserId(resultSet.getInt(fields[i]));
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
