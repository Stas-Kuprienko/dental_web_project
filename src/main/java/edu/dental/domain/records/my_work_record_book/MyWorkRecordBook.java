package edu.dental.domain.records.my_work_record_book;

import edu.dental.domain.entities.I_WorkRecord;
import edu.dental.domain.entities.Product;
import edu.dental.domain.entities.WorkRecord;
import edu.dental.domain.records.WorkRecordBook;
import edu.dental.domain.records.WorkRecordBookException;
import edu.dental.utils.DatesTool;
import edu.dental.utils.data_structures.MyList;

import java.time.LocalDate;
import java.util.NoSuchElementException;

/**
 * The class, implementing the {@link WorkRecordBook}.
 */
public class MyWorkRecordBook implements WorkRecordBook {

    private final MyList<I_WorkRecord> records;

    private final MyProductMap productMap;

    private MyWorkRecordBook(MyList<I_WorkRecord> records, MyProductMap productMap) {
        this.records = records;
        this.productMap = productMap;
    }

    private MyWorkRecordBook() {
        this.records = new MyList<>();
        this.productMap = new MyProductMap();
    }

    @Override
    public WorkRecord createRecord(String patient, String clinic, String product, int quantity, String complete) throws WorkRecordBookException {
        if (quantity > 32) {
            throw new WorkRecordBookException("The quantity value is incorrect - cannot be more than 32 teeth.");
        }
        Product p;
        LocalDate date;
        try {
            p = productMap.createProduct(product, quantity);
            date = DatesTool.toLocalDate(complete);
        } catch (NoSuchElementException | NullPointerException | IllegalArgumentException e) {
            throw new WorkRecordBookException(e.getMessage(), e.getCause());
        }
        WorkRecord workRecord = WorkRecord.create().setPatient(patient).setClinic(clinic).setComplete(date).build();
        workRecord.getProducts().add(p);
        records.add(workRecord);
        return workRecord;
    }

    @Override
    public WorkRecord createRecord(String patient, String clinic, String complete) throws WorkRecordBookException {
        LocalDate date;
        try {
            date = DatesTool.toLocalDate(complete);
        } catch (NullPointerException e) {
            throw new WorkRecordBookException(e.getMessage(), e.getCause());
        }
        WorkRecord workRecord = WorkRecord.create().setPatient(patient).setClinic(clinic).setComplete(date).build();
        records.add(workRecord);
        return workRecord;
    }

    @Override
    public I_WorkRecord addProductToRecord(I_WorkRecord workRecord, String product, int quantity) throws WorkRecordBookException {
        if (workRecord == null) {
            throw new WorkRecordBookException("The given WorkRecord object is null");
        }
        if (quantity > 32) {
            throw new WorkRecordBookException("The quantity value is incorrect - cannot be more than 32 teeth.");
        }
        Product p;
        try {
            p = productMap.createProduct(product, quantity);
        } catch (NoSuchElementException e) {
            throw new WorkRecordBookException("The specified product type is not found.", e.getCause());
        }
        workRecord.getProducts().add(p);
        return workRecord;
    }

    @Override
    public I_WorkRecord removeProduct(I_WorkRecord workRecord, String product) throws WorkRecordBookException {
        if ((workRecord == null)||(product == null || product.isEmpty())) {
            throw new WorkRecordBookException("The given argument is null or empty.");
        }
        if (workRecord.getProducts().isEmpty()) {
            return workRecord;
        }
        product = product.toLowerCase();
        for (Product p : workRecord.getProducts()) {
            if (p.title().equals(product)) {
                workRecord.getProducts().remove(p);
                break;
            }
        }
        return workRecord;
    }

    @Override
    public boolean deleteRecord(I_WorkRecord workRecord) {
        if (workRecord == null) {
            return false;
        }
        return records.remove(workRecord);
    }

    @Override
    public I_WorkRecord searchRecord(String patient, String clinic) throws WorkRecordBookException {
        try {
            MyList<I_WorkRecord> list = records.searchElement("patient", patient);
            if (list.size() > 1) {
                list = list.searchElement("clinic", clinic);
            }
            return list.get(0);
        } catch (NoSuchElementException | NullPointerException e) {
            throw new WorkRecordBookException(e.getMessage(), e.getCause());
        }
    }

    @Override
    public I_WorkRecord getByID(int id) throws WorkRecordBookException {
        try {
            MyList<I_WorkRecord> list = records.searchElement("id", String.valueOf(id));
            return list.get(0);
        } catch (NoSuchElementException | NullPointerException e) {
            throw new WorkRecordBookException(e.getMessage(), e.getCause());
        }
    }

    @Override
    public MyList<I_WorkRecord> sorting() {
        MyList<I_WorkRecord> result = new MyList<>();
        I_WorkRecord[] arr = new WorkRecord[records.size()];
        arr = records.toArray(arr);
        for (I_WorkRecord wr : arr) {
            if ((wr.getStatus()) == WorkRecord.Status.CLOSED) {
                //TODO
//                wr.setStatus();
                result.add(wr);
                records.remove(wr);
            } else if (DatesTool.isCurrentMonth(wr.getComplete(), PAY_DAY)) {
//                wr.setStatus(true);
                result.add(wr);
                records.remove(wr);
            }
        }
        return result;
    }

    @Override
    public MyList<I_WorkRecord> getList() {
        return records;
    }

    @Override
    public MyProductMap getMap() {
        return productMap;
    }
}
