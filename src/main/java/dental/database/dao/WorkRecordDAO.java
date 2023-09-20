package dental.database.dao;

import dental.domain.data_structures.MyList;
import dental.domain.userset.Account;
import dental.domain.works.Product;
import dental.domain.works.RecordManager;
import dental.domain.works.WorkRecord;
import dental.database.service.DBConfig;

import javax.imageio.ImageIO;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Constructor;
import java.sql.*;

public class WorkRecordDAO implements DAO<WorkRecord>{

    public WorkRecordDAO(Account account) {
        this.account = account;
    }

    private final Account account;
    public static final String TABLE_NAME = DBConfig.DATA_BASE + ".work_record_";
    private static final String PHOTO_FORMAT = "jpg";


    @Override
    public boolean insert(WorkRecord workRecord) throws Exception {
        //get string of all fields of the object
        String workFields = buildFieldToString();
        String[] metaData = workFields.split(", ");
        //concatenate string of values (?) for prepared statement
        String injectionCounts = "?, ".repeat(workFields.split(",").length - 1);
        injectionCounts = "DEFAULT, " + injectionCounts.substring(0, injectionCounts.length() - 2);

        String query = String.format(SQL_Sample.INSERT.QUERY, TABLE_NAME + account.getId(), workFields, injectionCounts);
        boolean isSuccess;
        int i = 1;
        try (DBRequest request =  new DBRequest(query)) {
            PreparedStatement statement = request.getStatement();
            statement.setString(i++, workRecord.getPatient());
            statement.setString(i++, workRecord.getClinic());
            //looping product columns and setting values
            while (!metaData[i].equals("complete")) {
                Product p = RecordManager.findProduct(workRecord, metaData[i]);
                int value = p == null ? 0 : p.quantity();
                statement.setInt(i++, value);
            }
            statement.setDate(i++, Date.valueOf(workRecord.getComplete()));
            statement.setDate(i++, Date.valueOf(workRecord.getAccepted()));
            statement.setBoolean(i++, workRecord.isClosed());
            statement.setBoolean(i++, workRecord.isPaid());
            if (workRecord.getPhoto() != null) {
                Blob blob = request.createBlob();
                try (OutputStream out = blob.setBinaryStream(1)) {
                    ImageIO.write(workRecord.getPhoto(), PHOTO_FORMAT, out);
                    statement.setBlob(i++, blob);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                statement.setBinaryStream(i++, null);
            }
            statement.setString(i, workRecord.getComment());
            statement.executeUpdate();
            isSuccess = DBRequest.setID(workRecord, statement);
        } catch (SQLException | NoSuchFieldException | IllegalAccessException e) {
            throw new Exception(e);
        }
        return isSuccess;
    }

    @Override
    public MyList<WorkRecord> getAll() throws SQLException, NoSuchMethodException {
        String query = String.format(SQL_Sample.SELECT_ALL.QUERY, "*", TABLE_NAME + account.getId());
        MyList<WorkRecord> workRecords;
        try (DBRequest request = new DBRequest(query)) {
            ResultSet resultSet = request.getStatement().executeQuery();
            workRecords = new WorkRecordInstantiation(resultSet).workRecords;
        } catch (SQLException e) {
            throw new SQLException(e.getCause());
        } catch (NoSuchMethodException e) {
            throw new NoSuchMethodException(e.getMessage());
        }
        return workRecords;
    }

    @Override
    public WorkRecord get(int id) throws Exception {
        return null;
    }

    @Override
    public boolean remove(int id) throws SQLException {
        String query = String.format(SQL_Sample.DELETE.QUERY, TABLE_NAME);
        boolean isSuccess;
        try (DBRequest request = new DBRequest(query)) {
            request.getStatement().setInt(1, id);
            isSuccess = request.getStatement().execute();
        } catch (SQLException e) {
            throw new SQLException(e);
        }
        return isSuccess;
    }

    private String buildFieldToString() {
        StringBuilder result = new StringBuilder();
        result.append("id, ");
        result.append("patient, ");
        result.append("clinic, ");
        String[] products = account.productMap.keysToArray();
        for (String p : products) {
            result.append(p).append(", ");
        }
        result.append("complete, ");
        result.append("accepted, ");
        result.append("closed, ");
        result.append("paid, ");
        result.append("photo, ");
        result.append("comment");
        return result.toString();
    }

    @SuppressWarnings("all")
    private class WorkRecordInstantiation extends Instantiation<WorkRecord> {

        private final ResultSet resultSet;
        private final MyList<WorkRecord> workRecords;
        private final Constructor<WorkRecord> constructor;

        private WorkRecordInstantiation(ResultSet resultSet) throws SQLException, NoSuchMethodException {
            try (resultSet) {
                this.resultSet = resultSet;
                workRecords = new MyList<>();
                constructor = WorkRecord.class.getDeclaredConstructor();
                constructor.setAccessible(true);
                build();
                constructor.setAccessible(false);
            } catch (SQLException e) {
                throw new SQLException(e);
            } catch (NoSuchMethodException e) {
                throw new NoSuchMethodException(e.getMessage());
            }
        }

        @Override
        protected void build() throws SQLException {
            while (resultSet.next()) {
                WorkRecord record = new WorkRecord(resultSet);
                MyList<Product> products = account.productMap.instantiateFromDB(resultSet);
                record.setProducts(products);
                workRecords.add(record);
            }
        }
    }

}
