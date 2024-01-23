package edu.dental.domain.records;

import edu.dental.database.DatabaseException;
import edu.dental.database.DatabaseService;
import edu.dental.domain.APIManager;
import edu.dental.entities.DentalWork;
import edu.dental.entities.Product;
import edu.dental.entities.ProductMap;
import edu.dental.entities.ProfitRecord;

import java.util.List;
import java.util.Map;

/**
 * The {@code WorkRecordBook} represents object is used for managing {@link DentalWork dental works}.
 * The implementation must contain a field - {@link List} of records
 *  and can {@code create, add, remove, search, sorting} for these objects.
 */
public interface WorkRecordBook {

    DatabaseService database = DatabaseService.getInstance();

    static WorkRecordBook createNew(int userId) {
        return APIManager.INSTANCE.getWorkRecordBook(userId);
    }

    static WorkRecordBook getInstance(int userId, List<DentalWork> works, ProductMap map) {
        return APIManager.INSTANCE.getWorkRecordBook(userId, works, map);
    }

    static WorkRecordBook getInstance(int userId) throws WorkRecordBookException {
        List<DentalWork> works;
        ProductMap map;
        try {
            works = database.getDentalWorkDAO().getAll(userId);
            //TODO
            map = APIManager.INSTANCE.getProductMap(userId);
        } catch (DatabaseException e) {
            throw new WorkRecordBookException(e);
        }
        return APIManager.INSTANCE.getWorkRecordBook(userId, works, map);
    }

    /**
     * Create a {@link DentalWork} object and add it to {@link List} field of this instance.
     * @param dentalWork {@link DentalWork} object to add.
     * @return The created DentalWork object.
     * @throws WorkRecordBookException if a given arguments is incorrect or the given product type
     *  is not contain in {@link Map}.
     */
    DentalWork addNewRecord(DentalWork dentalWork) throws WorkRecordBookException;

    ProductMap.Item addProductItem(String title, int price) throws WorkRecordBookException;

    Integer updateProductItem(String title, int price) throws WorkRecordBookException;

    boolean deleteProductItem(String title) throws WorkRecordBookException;

    DentalWork updateRecord(DentalWork dw, String field, String value) throws WorkRecordBookException;

    /**
     * Create {@link Product product} object and add it into the given {@link DentalWork}.
     *
     * @param dentalWork the {@link DentalWork} object to adding a {@link Product product}.
     * @param product    the product type title to add (should be containing in {@link Map}).
     * @param quantity   the quantity of a product items.
     * @return the updated given {@link DentalWork} object.
     * @throws WorkRecordBookException if a given arguments is incorrect or the given product type
     *                                 is not contain in {@link Map}.
     */
    DentalWork addProductToRecord(DentalWork dentalWork, String product, int quantity) throws WorkRecordBookException;

    /**
     * Remove {@link Product product} object from the given {@linkplain  DentalWork#getProducts() record}.
     *
     * @param dentalWork the {@link DentalWork} object to remove a {@link Product product}.
     * @param product    the product type title to remove (should be containing in {@link Map}).
     * @throws WorkRecordBookException if a given arguments is null.
     */
    void removeProduct(DentalWork dentalWork, String product) throws WorkRecordBookException;

    void removeProduct(int id, String product) throws WorkRecordBookException;

    /**
     * Delete {@link DentalWork} object from the instance {@link List} field.
     *
     * @param dentalWork the object to delete.
     */
    void deleteRecord(DentalWork dentalWork) throws WorkRecordBookException;

    void deleteRecord(int id) throws WorkRecordBookException;

    /**
     * Search a {@link DentalWork} by {@code patient and clinic} fields.
     * @param patient the patient name or surname of required record.
     * @param clinic the clinic  of required record.
     * @return the required DentalWork object.
     * @throws WorkRecordBookException if a given argument is incorrect or null, and if such object is not found.
     */
    DentalWork searchRecord(String patient, String clinic) throws WorkRecordBookException;

    DentalWork getById(int id, boolean includeDatabase) throws WorkRecordBookException;

    /**
     * Search a {@link DentalWork} by {@code id} fields.
     * @param id the id of required record.
     * @return the required {@link DentalWork} object or null if not found.
     */
    DentalWork getById(int id);

    List<DentalWork> getWorksByMonth(int monthValue, int year) throws WorkRecordBookException;

    List<DentalWork> searchRecordsInDatabase(String[] fields, String[] args) throws WorkRecordBookException;

    void sorting(int month, int year) throws WorkRecordBookException;

    ProfitRecord countProfitForMonth(int year, int monthValue) throws WorkRecordBookException;

    ProfitRecord[] countAllProfits() throws WorkRecordBookException;

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