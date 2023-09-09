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

public class WorkRecordDAO implements DAO<WorkRecord>{

    private WorkRecordDAO() {}
    static {
        instance = new WorkRecordDAO();
    }

    private static final WorkRecordDAO instance;
    private static final String TABLE_NAME = "work_record";

    @Override
    public void add(WorkRecord workRecord) throws SQLException {
        String query = String.format("INSERT INTO %s.%s (patient, clinic, complete, accepted, closed, photo, comment)" +
                "VALUES (?, ?, ?, ?, ?, ?, ?);", DBConfig.DATA_BASE, TABLE_NAME);
        DBRequest request = new DBRequest(query);
        PreparedStatement statement = request.getStatement();
        statement.setString(1, workRecord.getPatient());
        statement.setString(2, workRecord.getClinic());
        statement.setDate(3, Date.valueOf(workRecord.getComplete()));
        statement.setDate(4, Date.valueOf(workRecord.getAccepted()));
        statement.setBoolean(5, workRecord.isClosed());
        statement.setString(7, workRecord.getComment());
        try (FileInputStream fileIn = new FileInputStream(workRecord.getPhoto())) {
            byte[] photoBytes = IOUtils.toByteArray(fileIn);
            statement.setBlob(6, new SerialBlob(photoBytes));
        } catch (IOException e) {
            e.printStackTrace();
        }
        statement.execute();
        request.close();
        if (!(workRecord.getProducts().isEmpty())) {
            ProductDAO.getInstance().addAll(workRecord.getProducts());
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

    public static WorkRecordDAO getInstance() {
        return instance;
    }

    private class WorkRecordInstantiation extends Instantiation<WorkRecord> {

        @Override
        protected void build() throws Exception {

        }
    }

}
