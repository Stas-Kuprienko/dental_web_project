package dental.database.db_statements.select;

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


    public WorkRecordDBInstantiation() throws SQLException {
        super(SELECTABLE, FROM);
    }

    @Override
    public WorkRecordDBInstantiation build() throws
            SQLException, IllegalAccessException, NoSuchMethodException, InvocationTargetException, InstantiationException {

        Constructor<WorkRecord> workRecordConstructor = WorkRecord.class.getDeclaredConstructor();
        workRecordConstructor.setAccessible(true);
        while (result.next()) {
            String patient = result.getString(PATIENT);
            String clinic = result.getString(CLINIC);
            LocalDate complete = result.getDate(COMPLETE).toLocalDate();
            LocalDate accepted = result.getDate(ACCEPTED).toLocalDate();
            boolean closed = result.getBoolean(CLOSED);
            int id = result.getInt(ID);
            WorkRecord record = workRecordConstructor.newInstance();
            Field[] fields = record.getClass().getDeclaredFields();
            setObjectPrivateField(record, fields, PATIENT, patient);
            setObjectPrivateField(record, fields, CLINIC, clinic);
            setObjectPrivateField(record, fields, COMPLETE, complete);
            setObjectPrivateField(record, fields, ACCEPTED, accepted);
            record.setClosed(closed);
            MyList<Product> products = new ProductDBInstantiation(WORK_ID, String.valueOf(id)).build().getList();
            setObjectPrivateField(record, fields, PRODUCTS, products);
            list.add(record);
        }
        workRecordConstructor.setAccessible(false);
        return this;
    }
}
