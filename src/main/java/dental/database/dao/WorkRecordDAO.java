package dental.database.dao;

import dental.domain.MyList;
import dental.domain.works.Product;
import dental.domain.works.WorkRecord;
import dental.database.DBConfig;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Constructor;
import java.sql.*;

public class WorkRecordDAO implements DAO<WorkRecord>{

    public WorkRecordDAO(int accountId) {
        this.accountId = accountId;
    }

    private final int accountId;
    public static final String TABLE_NAME = DBConfig.DATA_BASE + ".work_record_";
    private static final String PHOTO_FORMAT = "jpg";

    private static final String WORK_FIELDS = "patient, clinic, complete, accepted, closed, photo, comment";

    @Override
    public boolean insert(WorkRecord workRecord) throws SQLException, NoSuchFieldException, IllegalAccessException {
        String query = String.format(SQL_DAO.INSERT.QUERY, "account_id, " + TABLE_NAME, WORK_FIELDS, "?".repeat(WORK_FIELDS.split(",").length));
        DBRequest request = new DBRequest(query);
        PreparedStatement statement = request.getStatement();
        statement.setInt(1, accountId);
        statement.setString(2, workRecord.getPatient());
        statement.setString(3, workRecord.getClinic());
        statement.setDate(4, Date.valueOf(workRecord.getComplete()));
        statement.setDate(5, Date.valueOf(workRecord.getAccepted()));
        statement.setBoolean(6, workRecord.isClosed());
        if (workRecord.getPhoto() != null) {
            Blob blob = request.createBlob();
            try (OutputStream out = blob.setBinaryStream(1)) {
                ImageIO.write(workRecord.getPhoto(), PHOTO_FORMAT, out);
                statement.setBlob(7, blob);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            statement.setBinaryStream(7, null);
        }
        statement.setString(8, workRecord.getComment());

        statement.executeUpdate();
        boolean isSuccess = DBRequest.setID(workRecord, statement);
        request.close();
        MyList<Product> products = workRecord.getProducts();
        if (!(products.isEmpty())) {
            ProductDAO productDAO = new ProductDAO(accountId, workRecord.getId());
            for (Product p : products) {
                isSuccess = productDAO.insert(p);
            }
        }
        return isSuccess;
    }

    @Override
    public MyList<WorkRecord> getAll() throws Exception {
        String query = String.format(SQL_DAO.SELECT_ALL.QUERY, "*", TABLE_NAME + accountId);
        DBRequest request = new DBRequest(query);
        ResultSet resultSet = request.getStatement().executeQuery();
        MyList<WorkRecord> workRecords = new WorkRecordInstantiation(resultSet).workRecords;
        request.close();
        return workRecords;
    }

    @Override
    public WorkRecord get(int id) throws Exception {
        return null;
    }

    @Override
    public boolean remove(int id) throws SQLException {
        String query = String.format(SQL_DAO.DELETE.QUERY, TABLE_NAME);
        DBRequest request = new DBRequest(query);
        request.getStatement().setInt(1, id);
        boolean isSuccess = request.getStatement().execute();
        request.close();
        return isSuccess;
    }

    @SuppressWarnings("all")
    private class WorkRecordInstantiation extends Instantiation<WorkRecord> {

        private final ResultSet resultSet;
        private final MyList<WorkRecord> workRecords;
        private final Constructor<WorkRecord> constructor;

        private WorkRecordInstantiation(ResultSet resultSet) throws Exception {

            this.resultSet = resultSet;
            workRecords = new MyList<>();
            constructor = WorkRecord.class.getDeclaredConstructor();
            constructor.setAccessible(true);
            build();
            constructor.setAccessible(false);
            resultSet.close();
        }

        @Override
        protected void build() throws Exception {
            String ID = "id";
            String PATIENT = "patient";
            String CLINIC = "clinic";
            String COMPLETE = "complete";
            String ACCEPTED = "accepted";
            String CLOSED = "closed";
            String PHOTO = "photo";
            String COMMENT = "comment";
            while(resultSet.next()) {
                WorkRecord workRecord = constructor.newInstance();
                workRecord.setPatient(resultSet.getString(PATIENT));
                workRecord.setClinic(resultSet.getString(CLINIC));
                workRecord.setComplete(resultSet.getDate(COMPLETE).toLocalDate());
                workRecord.setClosed(resultSet.getBoolean(CLOSED));
                Blob photoBlob = resultSet.getBlob(PHOTO);
                BufferedImage photo = ImageIO.read(photoBlob.getBinaryStream());
                workRecord.setPhoto(photo);
                workRecord.setComment(resultSet.getString(COMMENT));
                setObjectPrivateField(workRecord, ID, resultSet.getInt(ID));
                setObjectPrivateField(workRecord, ACCEPTED, resultSet.getDate(ACCEPTED).toLocalDate());
                MyList<Product> products = new ProductDAO(accountId, workRecord.getId()).getAll();
                setObjectPrivateField(workRecord, "products", products);
                workRecords.add(workRecord);
            }
        }

        private MyList<Product> instantiateProductList(ResultSet resultSet) {
            return null;
        }
    }

}
