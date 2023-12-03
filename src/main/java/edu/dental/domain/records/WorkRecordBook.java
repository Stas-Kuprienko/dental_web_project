package edu.dental.domain.records;

import edu.dental.database.DatabaseException;
import edu.dental.database.DatabaseService;
import edu.dental.domain.APIManager;
import edu.dental.domain.entities.DentalWork;
import edu.dental.domain.entities.Product;
import edu.dental.domain.entities.User;
import edu.dental.domain.entities.dto.DentalWorkDTO;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

/**
 * The {@code WorkRecordBook} represents object is used for managing {@link DentalWork records}.
 * The implementation must contain a field - {@link List} of records
 *  and can {@code create, add, remove, search, sorting} for these objects.
 */
public interface WorkRecordBook {

    static WorkRecordBook getInstance() {
        return APIManager.INSTANCE.getWorkRecordBook();
    }

    static WorkRecordBook getInstance(List<DentalWork> works, ProductMap map) {
        return APIManager.INSTANCE.getWorkRecordBook(works, map);
    }

    static WorkRecordBook getInstance(User user) throws WorkRecordBookException {
        DatabaseService db = APIManager.INSTANCE.getDatabaseService();
        List<DentalWork> works;
        ProductMap map;
        try {
            works = db.getDentalWorkDAO(user).getAll();
            map = APIManager.INSTANCE.getProductMap(user);
        } catch (DatabaseException e) {
            throw new WorkRecordBookException(e.getMessage(), e.getCause());
        }
        return APIManager.INSTANCE.getWorkRecordBook(works, map);
    }

    /**
     * The day of month, for {@linkplain WorkRecordBook#sorting()} method, is used like a border,
     *  when sorting {@link DentalWork}s by month.
     */
    byte PAY_DAY = 10;

    /**
     * Create a {@link DentalWork} object and add it to {@link List} field of this instance.
     * @param patient the patient name or surname of this work.
     * @param clinic the clinic of this work.
     * @param product the {@link edu.dental.domain.entities.Product product} type title
     *                 (that having in {@link Map}).
     * @param quantity the quantity of product items (no more than 32, depending on the type of product).
     * @param complete The date, when this work should be completed.
     * @return The created DentalWork object.
     * @throws WorkRecordBookException if a given arguments is incorrect or the given product type
     *  is not contain in {@link Map}.
     */
    DentalWork createRecord(String patient, String clinic, String product, int quantity, LocalDate complete) throws WorkRecordBookException;

    /**
     * Create a {@link DentalWork} object and add it to {@link List} field of this instance.
     * @param patient the patient name or surname of this work.
     * @param clinic the clinic of this work.
     * @return The created DentalWork object.
     */
    DentalWork createRecord(String patient, String clinic);

    /**
     * Create {@link edu.dental.domain.entities.Product product} object and add it into the given {@link DentalWork}.
     * @param dentalWork the {@link DentalWork} object to adding a {@link edu.dental.domain.entities.Product product}.
     * @param product the product type title to add (should be containing in {@link Map}).
     * @param quantity the quantity of a product items.
     * @return the modified given DentalWork object back.
     * @throws WorkRecordBookException if a given arguments is incorrect or the given product type
     *  is not contain in {@link Map}.
     */
    DentalWork addProductToRecord(DentalWork dentalWork, String product, int quantity) throws WorkRecordBookException;

    /**
     * Remove {@link edu.dental.domain.entities.Product product} object from the given {@linkplain  DentalWork#getProducts() record}.
     * @param dentalWork the {@link DentalWork} object to remove a {@link edu.dental.domain.entities.Product product}.
     * @param product the product type title to remove (should be containing in {@link Map}).
     * @return the modified given DentalWork object back.
     * @throws WorkRecordBookException if a given arguments is null.
     */
    DentalWork removeProduct(DentalWork dentalWork, String product) throws WorkRecordBookException;

    /**
     * Delete {@link DentalWork} object from the instance {@link List} field.
     * @param dentalWork the object to delete.
     * @return true if all successful.
     */
    boolean deleteRecord(DentalWork dentalWork);

    /**
     * Search a {@link DentalWork} by {@code patient and clinic} fields.
     * @param patient the patient name or surname of required record.
     * @param clinic the clinic  of required record.
     * @return the required DentalWork object.
     * @throws WorkRecordBookException if a given argument is incorrect or null, and if such object is not found.
     */
    DentalWork searchRecord(String patient, String clinic) throws WorkRecordBookException;

    /**
     * Search a {@link DentalWork} by {@code id} fields.
     * @param id the id of required record.
     * @return the required {@link DentalWork} object.
     * @throws WorkRecordBookException if a given argument is incorrect or null, and if such object is not found.
     */
    DentalWork getByID(int id) throws WorkRecordBookException;

    static Product findProduct(DentalWork dw, String type) {
        if (dw.getProducts().isEmpty()) {
            throw new NoSuchElementException("the given DentalWork(id=" + dw.getId() + ") doesn't has products.");
        }
        type = type.toLowerCase();
        for (Product p : dw.getProducts()) {
            if (p.title().equals(type)) {
                return p;
            }
        }
        return null;
    }

    static Product findProduct(DentalWorkDTO dw, String type) {
        if (dw.getProducts().length == 0) {
            throw new NoSuchElementException("the given DentalWork(id=" + dw.getId() + ") doesn't has products.");
        }
        type = type.toLowerCase();
        for (Product p : dw.getProducts()) {
            if (p.title().equals(type)) {
                return p;
            }
        }
        return null;
    }

    /**
     * Sort {@link DentalWork} objects in the instance {@link List} field by {@linkplain DentalWork#getStatus()}
     *  and {@linkplain DentalWork#getComplete()} values. If record is suited, it's set {@code closed}, removing from the instance list
     *  and add into new {@link List list}.
     * @return the {@link List} object of sorted closed records.
     */
    List<DentalWork> sorting();

    /**
     * Get the {@link List list} field of {@link DentalWork} objects.
     * @return {@link List} object, containing {@link DentalWork records}.
     */
    List<DentalWork> getList();

    /**
     * Get the {@link ProductMap} field of {@link edu.dental.domain.entities.Product products}.
     * @return {@link ProductMap} object, containing {@link edu.dental.domain.entities.Product product}
     *  {@link edu.dental.domain.records.ProductMap.Item entries}.
     */
    ProductMap getMap();
}
