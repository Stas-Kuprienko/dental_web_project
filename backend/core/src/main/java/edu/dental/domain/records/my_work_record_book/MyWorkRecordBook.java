package edu.dental.domain.records.my_work_record_book;

import edu.dental.database.DatabaseException;
import edu.dental.database.DatabaseService;
import edu.dental.domain.records.WorkRecordBook;
import edu.dental.domain.records.WorkRecordBookException;
import edu.dental.entities.DentalWork;
import edu.dental.entities.Product;
import edu.dental.entities.ProductMap;
import utils.collections.SimpleList;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * The class, implementing the {@link WorkRecordBook}.
 */
public class MyWorkRecordBook implements WorkRecordBook {

    private final int userId;

    private final SimpleList<DentalWork> records;

    private final MyProductMap productMap;

    @SuppressWarnings("unused")
    private MyWorkRecordBook(int userId, List<DentalWork> records, ProductMap productMap) {
        this.userId = userId;
        this.records = (SimpleList<DentalWork>) records;
        this.productMap = (MyProductMap) productMap;
    }

    @SuppressWarnings("unused")
    private MyWorkRecordBook(int userId) {
        this.userId = userId;
        this.records = new SimpleList<>();
        this.productMap = new MyProductMap();
    }

    @Override
    public DentalWork createRecord(String patient, String clinic, String product, int quantity, LocalDate complete) throws WorkRecordBookException {
        Product p;
        try {
            p = productMap.createProduct(product, quantity);
        } catch (NoSuchElementException | NullPointerException | IllegalArgumentException e) {
            throw new WorkRecordBookException(e.getMessage(), e);
        }
        DentalWork dentalWork = DentalWork.create().setPatient(patient).setClinic(clinic).setComplete(complete).build();
        dentalWork.getProducts().add(p);
        dentalWork.setUserId(userId);
        try {
            DatabaseService.getInstance().getDentalWorkDAO().put(dentalWork);
        } catch (DatabaseException e) {
            throw new WorkRecordBookException(e.getMessage(), e);
        }
        records.add(dentalWork);
        return dentalWork;
    }

    @Override
    public DentalWork createRecord(String patient, String clinic) throws WorkRecordBookException {
        DentalWork dentalWork = DentalWork.create().setPatient(patient).setClinic(clinic).build();
        dentalWork.setUserId(userId);
        try {
            DatabaseService.getInstance().getDentalWorkDAO().put(dentalWork);
        } catch (DatabaseException e) {
            throw new WorkRecordBookException(e.getMessage(), e);
        }
        records.add(dentalWork);
        return dentalWork;
    }

    @Override
    public ProductMap.Item addProductItem(String title, int price) throws WorkRecordBookException {
        try {
            int id = DatabaseService.getInstance().getProductMapDAO(userId).put(title, price);
            productMap.put(title, price, id);
            return productMap.getItem(title);
        } catch (DatabaseException e) {
            throw new WorkRecordBookException(e.getMessage(), e);
        }
    }

    @Override
    public Integer editProductItem(String title, int price) throws WorkRecordBookException {
        try {
            int id = productMap.getId(title);
            DatabaseService.getInstance().getProductMapDAO(userId).edit(id, price);
            return productMap.put(title, price);
        } catch (DatabaseException e) {
            throw new WorkRecordBookException(e.getMessage(), e);
        }
    }

    @Override
    public boolean deleteProductItem(String title) throws WorkRecordBookException {
        try {
            int id = productMap.remove(title);
            return DatabaseService.getInstance().getProductMapDAO(userId).delete(id);
        } catch (DatabaseException e) {
            throw new WorkRecordBookException(e.getMessage(), e);
        } catch (NullPointerException ignored) {
            return false;
        }
    }

    @Override
    public void editRecord(int id, String field, String value) throws WorkRecordBookException {
        DentalWork dw = getByID(id);
        editRecord(dw, field, value);
    }

    @Override
    public void editRecord(DentalWork dw, String field, String value) throws WorkRecordBookException {
        String oldValue = getValueFromWork(dw, field);
        Method setter = null;
        try {
            String setterName = concatenateSetMethod(field, "set");
            setter = DentalWork.class.getMethod(setterName, String.class);
            setter.invoke(dw, value);
            DatabaseService.getInstance().getDentalWorkDAO().edit(dw);
        } catch (DatabaseException e) {
            try {
                setter.invoke(dw, oldValue);
            } catch (IllegalAccessException | InvocationTargetException ex) {
                throw new WorkRecordBookException(ex.getMessage(), ex);
            }
            throw new WorkRecordBookException(e.getMessage(), e);
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new WorkRecordBookException(e.getMessage(), e);
        }
    }

    @Override
    public void addProductToRecord(DentalWork dentalWork, String product, int quantity) throws WorkRecordBookException {
        if (dentalWork == null || product == null) {
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
        try {
            DatabaseService.getInstance().getDentalWorkDAO().edit(dentalWork);
        } catch (DatabaseException e) {
            removeProduct(dentalWork, product);
            throw new WorkRecordBookException(e.getMessage(), e);
        }
    }

    @Override
    public void removeProduct(DentalWork dentalWork, String product) throws WorkRecordBookException {
        if ((dentalWork == null) || (product == null || product.isEmpty())) {
            throw new WorkRecordBookException("The given argument is null or empty.");
        }
        if (dentalWork.getProducts().isEmpty()) {
            return;
        }
        product = product.toLowerCase();
        Product removable = null;
        for (Product p : dentalWork.getProducts()) {
            if (p.title().equals(product)) {
                dentalWork.getProducts().remove(p);
                removable = p;
                break;
            }
        }
        if (removable != null) {
            try {
                DatabaseService.getInstance().getDentalWorkDAO().edit(dentalWork);
            } catch (DatabaseException e) {
                dentalWork.getProducts().add(removable);
                throw new WorkRecordBookException(e.getMessage(), e);
            }
        }
    }

    @Override
    public void removeProduct(int id, String product) throws WorkRecordBookException {
        DentalWork dw = getByID(id);
        removeProduct(dw, product);
    }

    @Override
    public void deleteRecord(DentalWork dentalWork) throws WorkRecordBookException {
        if (dentalWork == null) {
            return;
        }
        try {
            DatabaseService.getInstance().getDentalWorkDAO().delete(dentalWork.getId());
        } catch (DatabaseException e) {
            throw new WorkRecordBookException(e.getMessage(), e);
        }
        records.remove(dentalWork);
    }

    @Override
    public void deleteRecord(int id) throws WorkRecordBookException {
        deleteRecord(getByID(id));
    }

    @Override
    public DentalWork searchRecord(String patient, String clinic) throws WorkRecordBookException {
        try {
            SimpleList<DentalWork> list = records.searchElement("patient", patient);
            if (list.size() > 1) {
                list = list.searchElement("clinic", clinic);
                return list.get(0);
            }
            if (list.get(0).getClinic().equals(clinic)) {
                return list.get(0);
            }
            throw new WorkRecordBookException("such element is not found");
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
    public Sorter getSorter() {
        return new Sorter(records, userId);
    }

    @Override
    public int getUserId() {
        return userId;
    }

    public SimpleList<DentalWork> getRecords() {
        return records;
    }

    @Override
    public MyProductMap getProductMap() {
        return productMap;
    }


    private String getValueFromWork(DentalWork dw, String field) throws WorkRecordBookException {
        String getterName = concatenateSetMethod(field, "get");
        try {
            Method getter = DentalWork.class.getMethod(getterName);
            return String.valueOf(getter.invoke(dw));
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new WorkRecordBookException(e.getMessage(), e.getCause());
        }
    }

    private String concatenateSetMethod(String toModify, String method) {
        char firstLetter = (char) (toModify.charAt(0) - 32);
        StringBuilder str = new StringBuilder(toModify);
        str.setCharAt(0, firstLetter);
        str.insert(0, method, 0, method.length());
        return str.toString();
    }
}
