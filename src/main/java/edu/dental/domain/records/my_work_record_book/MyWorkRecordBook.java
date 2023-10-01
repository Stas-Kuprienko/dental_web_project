package edu.dental.domain.records.my_work_record_book;

import edu.dental.domain.entities.Product;
import edu.dental.domain.entities.WorkRecord;
import edu.dental.domain.records.ProductMapper;
import edu.dental.domain.records.WorkRecordBook;
import edu.dental.domain.records.WorkRecordBookException;
import edu.dental.utils.StringToDateConverter;
import edu.dental.utils.data_structures.MyList;

import java.time.LocalDate;
import java.util.NoSuchElementException;

/**
 * The class, implementing the {@link WorkRecordBook}.
 */
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
    public WorkRecord createRecord(String patient, String clinic, String product, int quantity, String complete) throws WorkRecordBookException {
        if (quantity > 32) {
            throw new WorkRecordBookException("The quantity value is incorrect - cannot be more than 32 teeth.");
        }
        Product p;
        LocalDate date;
        try {
            p = productMap.createProduct(product, quantity);
            date = StringToDateConverter.toLocalDate(complete);
        } catch (NoSuchElementException e) {
            throw new WorkRecordBookException("The specified product type is not found.", e.getCause());
        } catch (NullPointerException e) {
            throw new WorkRecordBookException(e.getMessage(), e.getCause());
        }
        WorkRecord workRecord = WorkRecord.create().setPatient(patient).setClinic(clinic).setComplete(date).build();
        workRecord.getProducts().add(p);
        return workRecord;
    }

    @Override
    public WorkRecord createRecord(String patient, String clinic, String complete) throws WorkRecordBookException {
        LocalDate date;
        try {
            date = StringToDateConverter.toLocalDate(complete);
        } catch (NullPointerException e) {
            throw new WorkRecordBookException(e.getMessage(), e.getCause());
        }
        return WorkRecord.create().setPatient(patient).setClinic(clinic).setComplete(date).build();
    }

    @Override
    public WorkRecord addProductToRecord(WorkRecord workRecord, String product, int quantity) throws WorkRecordBookException {
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
    public boolean deleteRecord(WorkRecord workRecord) {
        if (workRecord == null) {
            return false;
        }
        return records.remove(workRecord);
    }

    @Override
    public WorkRecord searchRecord(String patient, String clinic) throws WorkRecordBookException {
        if ((patient == null || patient.isEmpty())||(clinic == null || clinic.isEmpty())) {
            throw new WorkRecordBookException("The given argument is null or empty.");
        }
        try {
            MyList<WorkRecord> list = records.searchElement("patient", patient);
            if (list.isEmpty()) {
                throw new WorkRecordBookException("The specified element is not found.");
            }
            if (list.size() == 1) {
                return list.get(0);
            } else {
                list = list.searchElement("clinic", clinic);
                if (list.isEmpty()) {
                    throw new WorkRecordBookException("The specified element is not found.");
                } else {
                    return list.get(0);
                }
            }
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new WorkRecordBookException("The required element could not be found.", e.getCause());
        }
    }

    @Override
    public WorkRecord getByID(int id) throws WorkRecordBookException {
        try {
            MyList<WorkRecord> list = records.searchElement("id", String.valueOf(id));
            if (list.isEmpty()) {
                throw new WorkRecordBookException("The specified element is not found.");
            } else {
                return list.get(0);
            }
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new WorkRecordBookException(e.getMessage(), e.getCause());
        }
    }

}
