package edu.dental.domain.records;

import edu.dental.database.DatabaseException;
import edu.dental.database.DatabaseService;
import edu.dental.domain.APIManager;
import edu.dental.entities.DentalWork;
import edu.dental.entities.Product;
import edu.dental.entities.ProductMap;

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

    static WorkRecordBook createNew(int userId) {
        return APIManager.INSTANCE.getWorkRecordBook(userId);
    }

    static WorkRecordBook getInstance(int userId, List<DentalWork> works, ProductMap map) {
        return APIManager.INSTANCE.getWorkRecordBook(userId, works, map);
    }

    static WorkRecordBook getInstance(int userId) throws WorkRecordBookException {
        DatabaseService db = APIManager.INSTANCE.getDatabaseService();
        List<DentalWork> works;
        ProductMap map;
        try {
            works = db.getDentalWorkDAO().getAll(userId);
            //TODO
            map = APIManager.INSTANCE.getProductMap(userId);
        } catch (DatabaseException e) {
            throw new WorkRecordBookException(e.getMessage(), e.getCause());
        }
        return APIManager.INSTANCE.getWorkRecordBook(userId, works, map);
    }

    /**
     * Create a {@link DentalWork} object and add it to {@link List} field of this instance.
     * @param patient the patient name or surname of this work.
     * @param clinic the clinic of this work.
     * @param product the {@link Product product} type title
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
    DentalWork createRecord(String patient, String clinic) throws WorkRecordBookException;

    boolean addProductItem(String title, int price) throws WorkRecordBookException;

    Integer editProductItem(String title, int price) throws WorkRecordBookException;

    boolean deleteProductItem(String title) throws WorkRecordBookException;

    /**
     * Create {@link Product product} object and add it into the given {@link DentalWork}.
     * @param dentalWork the {@link DentalWork} object to adding a {@link Product product}.
     * @param product the product type title to add (should be containing in {@link Map}).
     * @param quantity the quantity of a product items.
     * @return the modified given DentalWork object back.
     * @throws WorkRecordBookException if a given arguments is incorrect or the given product type
     *  is not contain in {@link Map}.
     */
    DentalWork addProductToRecord(DentalWork dentalWork, String product, int quantity) throws WorkRecordBookException;

    /**
     * Remove {@link Product product} object from the given {@linkplain  DentalWork#getProducts() record}.
     * @param dentalWork the {@link DentalWork} object to remove a {@link Product product}.
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
    boolean deleteRecord(DentalWork dentalWork) throws WorkRecordBookException;

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

    /**
     * Sort {@link DentalWork} objects in the instance {@link List} field by {@linkplain DentalWork#getStatus()}
     *  and {@linkplain DentalWork#getComplete()} values. If record is suited, it's set {@code closed}, removing from the instance list
     *  and add into new {@link List list}.
     * @return the {@link List} object of sorted closed records.
     */
    List<DentalWork> sorting(int month);

    int getUserId();

    /**
     * Get the {@link List list} field of {@link DentalWork} objects.
     * @return {@link List} object, containing {@link DentalWork records}.
     */
    List<DentalWork> getRecords();

    /**
     * Get the {@link ProductMap} field of {@link Product products}.
     * @return {@link ProductMap} object, containing {@link Product product}
     *  {@link ProductMap.Item entries}.
     */
    ProductMap getProductMap();
}
