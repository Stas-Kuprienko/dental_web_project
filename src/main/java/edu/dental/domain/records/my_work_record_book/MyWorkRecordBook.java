package edu.dental.domain.records.my_work_record_book;

import edu.dental.database.dao.DAO;
import edu.dental.domain.entities.WorkRecord;
import edu.dental.domain.records.ProductMapper;
import edu.dental.domain.records.WorkRecordBook;
import edu.dental.utils.data_structures.MyList;

import java.time.LocalDate;
import java.util.Collection;

public class MyWorkRecordBook implements WorkRecordBook {

    public final MyList<WorkRecord> records;

    public final ProductMapper productMap;

    private MyWorkRecordBook(MyList<WorkRecord> records, ProductMapper productMap) {
        this.records = records;
        this.productMap = productMap;
    }

    private MyWorkRecordBook() {
        this.records = new MyList<>();
        this.productMap = new ProductMapper();
    }

    @Override
    public WorkRecord createRecord(String patient, String clinic, String product, int quantity, LocalDate complete) {

        return null;
    }

    @Override
    public WorkRecord createRecord(String patient, String clinic, LocalDate complete) {
        return null;
    }

    @Override
    public WorkRecord addProductToRecord(WorkRecord workRecord, String product, int quantity) {
        return null;
    }

    @Override
    public boolean deleteRecord(WorkRecord workRecord) {
        return false;
    }

    @Override
    public WorkRecord searchRecord(String patient, String clinic) {
        return null;
    }

    @Override
    public WorkRecord getByID(int id) {
        return null;
    }


    protected static class WorkRecordInstantiation implements DAO.Instantiating<WorkRecord> {

        @Override
        public Collection<WorkRecord> build() {
            return null;
        }
    }

}
