package dental.database.dao;

import dental.app.MyList;
import dental.app.works.WorkRecord;
import dental.database.DBConfig;
import org.apache.tomcat.jakartaee.commons.io.IOUtils;

import javax.sql.rowset.serial.SerialBlob;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

public class WorkRecordDAO implements DAO<WorkRecord>{

    public WorkRecordDAO(int accountId) {
        this.accountId = accountId;
    }

    private final int accountId;
    public static final String TABLE_NAME = "work_record";

    @Override
    public void add(WorkRecord workRecord) throws SQLException, NoSuchFieldException, IllegalAccessException {
        String query = String.format(
                "INSERT INTO %s.%s (account_id, patient, clinic, complete, accepted, closed, photo, comment)" +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?);", DBConfig.DATA_BASE, TABLE_NAME);
        DBRequest request = new DBRequest(query);
        PreparedStatement statement = request.getStatement();
        statement.setInt(1, accountId);
        statement.setString(2, workRecord.getPatient());
        statement.setString(3, workRecord.getClinic());
        statement.setDate(4, Date.valueOf(workRecord.getComplete()));
        statement.setDate(5, Date.valueOf(workRecord.getAccepted()));
        statement.setBoolean(6, workRecord.isClosed());
        if (workRecord.getPhoto() != null) {
            try (FileInputStream fileIn = new FileInputStream(workRecord.getPhoto())) {
                byte[] photoBytes = IOUtils.toByteArray(fileIn);
                statement.setBlob(7, new SerialBlob(photoBytes));
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            statement.setBinaryStream(7, null);
        }
        statement.setString(8, workRecord.getComment());
        statement.executeUpdate();
        DBRequest.setID(workRecord, statement);
        request.close();
        if (!(workRecord.getProducts().isEmpty())) {
            ProductDAO productDAO = new ProductDAO(workRecord.getId());
            productDAO.add(workRecord.getProducts().get(0));
        }
    }

    @Override
    public MyList<WorkRecord> getAll() throws Exception {
        return null;
    }

    @Override
    public WorkRecord get(int id) throws Exception {
        return null;
    }

    @Override
    public void remove(int id) throws SQLException {

    }

    @Override
    public void remove(WorkRecord workRecord) throws SQLException {

    }

    public int getAccountId() {
        return accountId;
    }

    private class WorkRecordInstantiation extends Instantiation<WorkRecord> {

        @Override
        protected void build() throws Exception {

        }
    }

}
