package edu.dental.domain.records.my_work_record_book;

import edu.dental.domain.entities.DentalWork;
import edu.dental.domain.entities.I_DentalWork;
import edu.dental.domain.entities.Product;
import edu.dental.domain.records.ProductMap;
import edu.dental.domain.records.WorkRecordBook;
import edu.dental.domain.records.WorkRecordBookException;
import edu.dental.utils.DatesTool;
import edu.dental.utils.data_structures.MyList;

import java.time.LocalDate;
import java.util.Collection;
import java.util.NoSuchElementException;

/**
 * The class, implementing the {@link WorkRecordBook}.
 */
public class MyWorkRecordBook implements WorkRecordBook {

    private final MyList<I_DentalWork> records;

    private final MyProductMap productMap;

    private MyWorkRecordBook(Collection<I_DentalWork> records, ProductMap productMap) {
        this.records = (MyList<I_DentalWork>) records;
        this.productMap = (MyProductMap) productMap;
    }

    private MyWorkRecordBook() {
        this.records = new MyList<>();
        this.productMap = new MyProductMap();
    }

    @Override
    public DentalWork createRecord(String patient, String clinic, String product, int quantity, String complete) throws WorkRecordBookException {
        Product p;
        LocalDate date;
        try {
            p = productMap.createProduct(product, quantity);
            date = DatesTool.toLocalDate(complete);
        } catch (NoSuchElementException | NullPointerException | IllegalArgumentException e) {
            throw new WorkRecordBookException(e.getMessage(), e.getCause());
        }
        DentalWork dentalWork = DentalWork.create().setPatient(patient).setClinic(clinic).setComplete(date).build();
        dentalWork.getProducts().add(p);
        records.add(dentalWork);
        return dentalWork;
    }

    @Override
    public DentalWork createRecord(String patient, String clinic) {
        DentalWork dentalWork = DentalWork.create().setPatient(patient).setClinic(clinic).build();
        records.add(dentalWork);
        return dentalWork;
    }

    @Override
    public I_DentalWork addProductToRecord(I_DentalWork dentalWork, String product, int quantity) throws WorkRecordBookException {
        if (dentalWork == null) {
            throw new WorkRecordBookException("The given DentalWork object is null");
        }
        Product p;
        try {
            p = productMap.createProduct(product, quantity);
        } catch (IllegalArgumentException | NoSuchElementException e) {
            throw new WorkRecordBookException(e.getMessage(), e.getCause());
        }
        dentalWork.getProducts().add(p);
        return dentalWork;
    }

    @Override
    public I_DentalWork removeProduct(I_DentalWork dentalWork, String product) throws WorkRecordBookException {
        if ((dentalWork == null)||(product == null || product.isEmpty())) {
            throw new WorkRecordBookException("The given argument is null or empty.");
        }
        if (dentalWork.getProducts().isEmpty()) {
            return dentalWork;
        }
        product = product.toLowerCase();
        for (Product p : dentalWork.getProducts()) {
            if (p.title().equals(product)) {
                dentalWork.getProducts().remove(p);
                break;
            }
        }
        return dentalWork;
    }

    @Override
    public boolean deleteRecord(I_DentalWork dentalWork) {
        if (dentalWork == null) {
            return false;
        }
        return records.remove(dentalWork);
    }

    @Override
    public I_DentalWork searchRecord(String patient, String clinic) throws WorkRecordBookException {
        try {
            MyList<I_DentalWork> list = records.searchElement("patient", patient);
            if (list.size() > 1) {
                list = list.searchElement("clinic", clinic);
            }
            return list.get(0);
        } catch (NoSuchElementException | NullPointerException e) {
            throw new WorkRecordBookException(e.getMessage(), e.getCause());
        }
    }

    @Override
    public I_DentalWork getByID(int id) throws WorkRecordBookException {
        try {
            MyList<I_DentalWork> list = records.searchElement("id", String.valueOf(id));
            return list.get(0);
        } catch (NoSuchElementException | NullPointerException e) {
            throw new WorkRecordBookException(e.getMessage(), e.getCause());
        }
    }

    @Override
    public MyList<I_DentalWork> sorting() {
        MyList<I_DentalWork> result = new MyList<>();
        for (I_DentalWork dw : records) {
            if (dw.getStatus().ordinal() > 0) {
                result.add(dw);
                records.remove(dw);
            } else if (DatesTool.isCurrentMonth(dw.getComplete(), PAY_DAY)) {
                dw.setStatus(I_DentalWork.Status.CLOSED);
                result.add(dw);
                records.remove(dw);
            }
        }
        return result;
    }

    @Override
    public MyList<I_DentalWork> getList() {
        return records;
    }

    @Override
    public MyProductMap getMap() {
        return productMap;
    }
}
