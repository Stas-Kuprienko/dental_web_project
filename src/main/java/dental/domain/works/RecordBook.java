package dental.domain.works;

import dental.domain.data_structures.MyList;

import java.time.LocalDate;

public class RecordBook extends AbstractRecordBook<WorkRecord> {

    public final MyList<WorkRecord> records;

    public final ProductMapper productMap;

    public RecordBook(MyList<WorkRecord> records, ProductMapper productMap) {
        this.records = records;
        this.productMap = productMap;
    }

    public RecordBook() {
        this.records = new MyList<>();
        this.productMap = new ProductMapper();
    }

    @Override
    public boolean createRecord(String patient, String clinic, String product, int quantity, LocalDate complete) {
        return false;
    }

    @Override
    public boolean createRecord(String patient, String clinic, LocalDate complete) {
        return false;
    }

    @Override
    public boolean addProductToRecord(int id, String product, int quantity) {
        return false;
    }

    @Override
    public boolean deleteRecord(int id) {
        return false;
    }

    @Override
    public WorkRecord getRecord(String patient, String clinic) {
        return null;
    }

    @Override
    public WorkRecord getByID(int id) {
        return null;
    }

    @Override
    public boolean editRecord(int id, Fields field, Object value) {
        return false;
    }

}
