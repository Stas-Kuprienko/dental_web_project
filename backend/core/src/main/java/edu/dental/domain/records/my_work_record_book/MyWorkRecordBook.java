package edu.dental.domain.records.my_work_record_book;

import edu.dental.entities.DentalWork;
import edu.dental.entities.Product;
import edu.dental.entities.ProductMap;
import edu.dental.domain.records.WorkRecordBook;
import edu.dental.domain.records.WorkRecordBookException;
import edu.dental.domain.utils.DatesTool;
import utils.collections.SimpleList;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * The class, implementing the {@link WorkRecordBook}.
 */
public class MyWorkRecordBook implements WorkRecordBook {

    private final SimpleList<DentalWork> records;

    private final MyProductMap productMap;

    @SuppressWarnings("unused")
    private MyWorkRecordBook(List<DentalWork> records, ProductMap productMap) {
        this.records = (SimpleList<DentalWork>) records;
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
    public DentalWork addProductToRecord(DentalWork dentalWork, String product, int quantity) throws WorkRecordBookException {
        if (dentalWork == null) {
            throw new WorkRecordBookException("The given DentalWork object is null");
        }
        Product p;
        try {
            SimpleList<Product> products = dentalWork.getProducts();
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
    public DentalWork removeProduct(DentalWork dentalWork, String product) throws WorkRecordBookException {
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
    public boolean deleteRecord(DentalWork dentalWork) {
        if (dentalWork == null) {
            return false;
        }
        return records.remove(dentalWork);
    }

    @Override
    public DentalWork searchRecord(String patient, String clinic) throws WorkRecordBookException {
        try {
            SimpleList<DentalWork> list = records.searchElement("patient", patient);
            if (list.size() > 1) {
                list = list.searchElement("clinic", clinic);
            }
            return list.get(0);
        } catch (NoSuchElementException | NullPointerException e) {
            throw new WorkRecordBookException(e.getMessage(), e.getCause());
        }
    }

    @Override
    public DentalWork getByID(int id) throws WorkRecordBookException {
        if (records.size() == 0) {
            throw new WorkRecordBookException("The DentalWork list is empty.");
        }
        DentalWork[] works = new DentalWork[records.size()];
        Arrays.sort(records.toArray(works));
        DentalWork dw = new DentalWork();
        dw.setId(id);
        int i = Arrays.binarySearch(records.toArray(), dw);
        if (i < 0) {
            throw new WorkRecordBookException("The required object is not found (id=" + id + ").");
        } else {
            return works[i];
        }
    }

    @Override
    public SimpleList<DentalWork> sorting() {
        SimpleList<DentalWork> result = new SimpleList<>();
        LocalDate today = LocalDate.now();
        for (DentalWork dw : records.toArray(new DentalWork[]{})) {
            if (dw.getStatus().ordinal() > 1) {
                records.remove(dw);
            } else if (dw.getComplete().isBefore(today)) {
                dw.setStatus(DentalWork.Status.CLOSED);
                result.add(dw);
                if (!(DatesTool.isCurrentMonth(dw.getComplete(), PAY_DAY))) {
                    records.remove(dw);
                }
            }
        }
        return result;
    }

    @Override
    public SimpleList<DentalWork> getList() {
        return records;
    }

    @Override
    public MyProductMap getMap() {
        return productMap;
    }
}
