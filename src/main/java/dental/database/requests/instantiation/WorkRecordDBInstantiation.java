package dental.database.requests.instantiation;

import dental.app.MyList;
import dental.app.works.Product;
import dental.app.works.WorkRecord;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.time.LocalDate;

public class WorkRecordDBInstantiation extends ObjectDBPrototype<WorkRecord> {

    private static final String SELECTABLE = "*";
    private static final String FROM = "work_records";

    private static final String PATIENT = "patient";
    private static final String CLINIC = "clinic";
    private static final String COMPLETE = "complete";
    private static final String ACCEPTED = "accepted";
    private static final String CLOSED = "closed";
    private static final String ID = "id";
    private static final String WORK_ID = "work_id";
    private static final String PRODUCTS = "products";


    public WorkRecordDBInstantiation(String whereField, String whereValue) throws SQLException {
        super(SELECTABLE, FROM, whereField, whereValue);
    }

    public WorkRecordDBInstantiation() throws SQLException {
        super(SELECTABLE, FROM);
    }

    @Override
    public WorkRecordDBInstantiation build() throws
            SQLException, IllegalAccessException, NoSuchMethodException, InvocationTargetException, InstantiationException {

        Constructor<WorkRecord> workRecordConstructor = WorkRecord.class.getDeclaredConstructor();
        workRecordConstructor.setAccessible(true);
        while (resultSet.next()) {
            String patient = resultSet.getString(PATIENT);
            String clinic = resultSet.getString(CLINIC);
            LocalDate complete = resultSet.getDate(COMPLETE).toLocalDate();
            LocalDate accepted = resultSet.getDate(ACCEPTED).toLocalDate();
            boolean closed = resultSet.getBoolean(CLOSED);
            int id = resultSet.getInt(ID);
            WorkRecord workRecord = workRecordConstructor.newInstance();
            Field[] fields = workRecord.getClass().getDeclaredFields();
            workRecord.setPatient(patient);
            workRecord.setClinic(clinic);
            workRecord.setComplete(complete);
            workRecord.setClosed(closed);
            setObjectPrivateField(workRecord, fields, ACCEPTED, accepted);
            MyList<Product> products = new ProductDBInstantiation(WORK_ID, String.valueOf(id)).build().getList();
            setObjectPrivateField(workRecord, fields, PRODUCTS, products);
            list.add(workRecord);
        }
        workRecordConstructor.setAccessible(false);
        return this;
    }
}
