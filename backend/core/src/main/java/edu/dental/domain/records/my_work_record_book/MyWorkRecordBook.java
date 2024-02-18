package edu.dental.domain.records.my_work_record_book;

import edu.dental.database.DatabaseException;
import edu.dental.database.DatabaseService;
import edu.dental.database.dao.ProfitRecordDAO;
import edu.dental.domain.records.SorterTool;
import stas.utilities.UpdateFacility;
import edu.dental.domain.records.WorkRecordBook;
import edu.dental.domain.records.WorkRecordException;
import edu.dental.entities.DentalWork;
import edu.dental.entities.Product;
import edu.dental.entities.ProductMap;
import edu.dental.entities.ProfitRecord;
import stas.collections.SimpleList;

import java.time.LocalDate;
import java.time.Month;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.logging.Level;

/**
 * The class, implementing the {@link WorkRecordBook}.
 */
public class MyWorkRecordBook implements WorkRecordBook {

    private final DatabaseService databaseService;

    private final int userId;

    private final SimpleList<DentalWork> records;

    private final MyProductMap productMap;

    @SuppressWarnings("unused")
    private MyWorkRecordBook(int userId, List<DentalWork> records, ProductMap productMap) {
        this.userId = userId;
        this.records = (SimpleList<DentalWork>) records;
        this.productMap = (MyProductMap) productMap;
        this.databaseService = DatabaseService.getInstance();
    }

    @SuppressWarnings("unused")
    private MyWorkRecordBook(int userId) {
        this.userId = userId;
        this.records = new SimpleList<>();
        this.productMap = new MyProductMap();
        this.databaseService = DatabaseService.getInstance();
    }

    @Override
    public DentalWork addNewRecord(String patient, String clinic, String productItem, int quantity, LocalDate complete) throws DatabaseException, WorkRecordException {
        DentalWork newRecord = DentalWork.create()
                .setUserId(userId)
                .setPatient(patient)
                .setClinic(clinic)
                .setComplete(complete)
                .build();

        try {
            Product product = productMap.createProduct(productItem, quantity);
            newRecord.getProducts().add(product);
            databaseService.getDentalWorkDAO().put(newRecord);
            records.add(newRecord);
            return newRecord;
        } catch (IllegalArgumentException | NullPointerException e) {
            throw new WorkRecordException(e);
        }
    }

    @Override
    public DentalWork addNewRecord(String patient, String clinic) throws DatabaseException {
        DentalWork newRecord = DentalWork.create()
                .setUserId(userId)
                .setPatient(patient)
                .setClinic(clinic)
                .build();

        databaseService.getDentalWorkDAO().put(newRecord);
        records.add(newRecord);
        return newRecord;
    }

    @Override
    public ProductMap.Item addProductItem(String title, int price) throws DatabaseException {
        int id = databaseService.getProductMapDAO(userId).put(title, price);
        productMap.put(title, price, id);
        return productMap.getItem(title);
    }

    @Override
    public Integer updateProductItem(String title, int price) throws DatabaseException {
        int id = productMap.getId(title);
        databaseService.getProductMapDAO(userId).edit(id, price);
        return productMap.put(title, price);
    }

    @Override
    public boolean deleteProductItem(String title) throws DatabaseException {
        try {
            int id = productMap.remove(title);
            return databaseService.getProductMapDAO(userId).delete(id);
        } catch (NullPointerException ignored) {
            return false;
        }
    }

    @Override
    public DentalWork updateRecord(DentalWork dw, String field, String value) throws WorkRecordException, DatabaseException {
        UpdateFacility<DentalWork> updateFacility = new UpdateFacility<>();
        try {
            updateFacility.init(dw, field);
            updateFacility.setNewValue(value);
            databaseService.getDentalWorkDAO().update(dw);
        } catch (ReflectiveOperationException e) {
            throw new WorkRecordException(e);
        } catch (DatabaseException e) {
            try {
                updateFacility.revert();
                throw e;
            } catch (ReflectiveOperationException e1) {
                throw new WorkRecordException(e1);
            }
        }
        return dw;
    }

    @Override
    public DentalWork addProductToRecord(DentalWork dentalWork, String product, int quantity) throws WorkRecordException, DatabaseException {
        if (dentalWork == null || product == null) {
            throw new WorkRecordException(new NullPointerException("The given DentalWork object is null"));
        }
        Product p;
        SimpleList<Product> products = dentalWork.getProducts();
        try {
            p = products.searchElement("title", product).get(0);
            quantity += p.quantity();
            removeProduct(dentalWork, product);
        } catch (NoSuchElementException ignored) {}
        try {
            p = productMap.createProduct(product, quantity);
            dentalWork.getProducts().add(p);
            databaseService.getDentalWorkDAO().update(dentalWork);
        } catch (IllegalArgumentException | NoSuchElementException e) {
            throw new WorkRecordException(e);
        } catch (DatabaseException e) {
            removeProduct(dentalWork, product);
            throw e;
        }
        return dentalWork;
    }

    @Override
    public void removeProduct(DentalWork dentalWork, String product) throws WorkRecordException, DatabaseException {
        if ((dentalWork == null) || (product == null || product.isEmpty())) {
            throw new WorkRecordException(new NullPointerException("The given argument is null or empty"));
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
                databaseService.getDentalWorkDAO().update(dentalWork);
            } catch (DatabaseException e) {
                dentalWork.getProducts().add(removable);
                throw e;
            }
        }
    }

    @Override
    public void removeProduct(int id, String product) throws WorkRecordException, DatabaseException {
        DentalWork dw = getById(id);
        removeProduct(dw, product);
    }

    @Override
    public void deleteRecord(DentalWork dentalWork) throws DatabaseException {
        if (dentalWork == null) {
            return;
        }
        databaseService.getDentalWorkDAO().delete(dentalWork.getId());
        records.remove(dentalWork);
    }

    @Override
    public void deleteRecord(int id) throws DatabaseException {
        deleteRecord(getById(id));
    }

    @Override
    public DentalWork searchRecord(String patient, String clinic) throws WorkRecordException {
        try {
            SimpleList<DentalWork> list = records.searchElement("patient", patient);
            if (list.size() > 1) {
                list = list.searchElement("clinic", clinic);
                return list.get(0);
            }
            if (list.get(0).getClinic().equals(clinic)) {
                return list.get(0);
            }
            throw new WorkRecordException(new NoSuchElementException(patient + ", " + clinic), Level.INFO);
        } catch (NoSuchElementException | NullPointerException e) {
            throw new WorkRecordException(e, Level.INFO);
        }
    }

    @Override
    public DentalWork getById(int id, boolean includeDatabase) throws DatabaseException {
        DentalWork dw = getById(id);
        if (dw == null && includeDatabase) {
            dw = databaseService.getDentalWorkDAO().get(userId, id);
        }
        return dw;
    }

    @Override
    public DentalWork getById(int id) {
        if (records.size() == 0) {
            return null;
        }
        DentalWork[] works = new DentalWork[records.size()];
        Arrays.sort(records.toArray(works));
        DentalWork dw = new DentalWork();
        dw.setId(id);
        int i = Arrays.binarySearch(records.toArray(), dw);
        if (i < 0) {
            return null;
        } else {
            return works[i];
        }
    }

    @Override
    public List<DentalWork> getWorksByMonth(int monthValue, int year) throws DatabaseException {
        String month = Month.of(monthValue).toString();
        return databaseService.getDentalWorkDAO().getAllMonthly(userId, month, year);
    }

    @Override
    public List<DentalWork> searchRecordsInDatabase(String[] fields, String[] args) throws WorkRecordException, DatabaseException {
        if (fields.length != args.length || fields.length == 0) {
            throw new WorkRecordException(new IllegalArgumentException("arrays of arguments is not equals by length or empty"));
        }
        return databaseService.getDentalWorkDAO().search(userId, fields, args);
    }

    @Override
    public void sorting(int month, int year) throws DatabaseException {
        SorterTool<DentalWork> sorter = new Sorter(userId, month, year);
        sorter.push(records);
        sorter.doIt();
    }

    @Override
    public ProfitRecord countProfitForMonth(int year, int monthValue) throws DatabaseException {
        String month = Month.of(monthValue).toString().toLowerCase();
        ProfitRecordDAO dao = databaseService.getProfitRecordDAO();
        return dao.countProfitForMonth(userId, year, month);
    }

    @Override
    public ProfitRecord[] countAllProfits() throws DatabaseException {
        return databaseService.getProfitRecordDAO().countAllProfits(userId);
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
}