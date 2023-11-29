package edu.dental.domain.records.my_work_record_book;

import edu.dental.domain.DatesTool;
import edu.dental.domain.entities.DentalWork;
import edu.dental.domain.entities.IDentalWork;
import edu.dental.domain.entities.Product;
import edu.dental.domain.records.ProductMap;
import edu.dental.domain.records.WorkRecordBook;
import edu.dental.domain.records.WorkRecordBookException;
import utils.collections.SimpleList;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * The class, implementing the {@link WorkRecordBook}.
 */
public class MyWorkRecordBook implements WorkRecordBook {

    private final SimpleList<IDentalWork> records;

    private final MyProductMap productMap;

    @SuppressWarnings("unused")
    private MyWorkRecordBook(List<IDentalWork> records, ProductMap productMap) {
        this.records = (SimpleList<IDentalWork>) records;
        this.productMap = (MyProductMap) productMap;
    }

    @SuppressWarnings("unused")
    private MyWorkRecordBook() {
        this.records = new SimpleList<>();
        this.productMap = new MyProductMap();
    }

    @Override
    public DentalWork createRecord(String patient, String clinic, String product, int quantity, LocalDate complete) throws WorkRecordBookException {
        Product p;
        try {
            p = productMap.createProduct(product, quantity);
        } catch (NoSuchElementException | NullPointerException | IllegalArgumentException e) {
            throw new WorkRecordBookException(e.getMessage(), e.getCause());
        }
        DentalWork dentalWork = DentalWork.create().setPatient(patient).setClinic(clinic).setComplete(complete).build();
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
    public IDentalWork addProductToRecord(IDentalWork dentalWork, String product, int quantity) throws WorkRecordBookException {
        if (dentalWork == null) {
            throw new WorkRecordBookException("The given DentalWork object is null");
        }
        Product p;
        try {
            SimpleList<Product> products = (SimpleList<Product>) dentalWork.getProducts();
            p = products.searchElement("title", product).get(0);
            quantity += p.quantity();
            removeProduct(dentalWork, product);
        } catch (NullPointerException | NoSuchElementException ignored) {}
        try {
            p = productMap.createProduct(product, quantity);
        } catch (IllegalArgumentException | NoSuchElementException e) {
            throw new WorkRecordBookException(e.getMessage(), e.getCause());
        }
        dentalWork.getProducts().add(p);
        return dentalWork;
    }

    @Override
    public IDentalWork removeProduct(IDentalWork dentalWork, String product) throws WorkRecordBookException {
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
    public boolean deleteRecord(IDentalWork dentalWork) {
        if (dentalWork == null) {
            return false;
        }
        return records.remove(dentalWork);
    }

    @Override
    public IDentalWork searchRecord(String patient, String clinic) throws WorkRecordBookException {
        try {
            SimpleList<IDentalWork> list = records.searchElement("patient", patient);
            if (list.size() > 1) {
                list = list.searchElement("clinic", clinic);
            }
            return list.get(0);
        } catch (NoSuchElementException | NullPointerException e) {
            throw new WorkRecordBookException(e.getMessage(), e.getCause());
        }
    }

    @Override
    public IDentalWork getByID(int id) throws WorkRecordBookException {
        if (records.size() == 0) {
            throw new WorkRecordBookException("The DentalWork list is empty.");
        }
        IDentalWork[] works = new IDentalWork[records.size()];
        Arrays.sort(records.toArray(works));
        IDentalWork dw = new DentalWork();
        dw.setId(id);
        int i = Arrays.binarySearch(records.toArray(), dw);
        if (i < 0) {
            throw new WorkRecordBookException("The required object is not found (id=" + id + ").");
        } else {
            return works[i];
        }
    }

    @Override
    public SimpleList<IDentalWork> sorting() {
        SimpleList<IDentalWork> result = new SimpleList<>();
        for (IDentalWork dw : records.toArray(new IDentalWork[]{})) {
            if (dw.getStatus().ordinal() > 0) {
                result.add(dw);
                records.remove(dw);
            } else if (DatesTool.isCurrentMonth(dw.getComplete(), PAY_DAY)) {
                dw.setStatus(IDentalWork.Status.CLOSED);
                result.add(dw);
                records.remove(dw);
            }
        }
        return result;
    }

    @Override
    public SimpleList<IDentalWork> getList() {
        return records;
    }

    @Override
    public MyProductMap getMap() {
        return productMap;
    }
}
