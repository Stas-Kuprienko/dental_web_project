package edu.dental.domain.records;

import edu.dental.domain.entities.DentalWork;
import edu.dental.domain.entities.IDentalWork;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * The {@code WorkRecordBook} represents object is used for managing {@link DentalWork records}.
 * The implementation must contain a field - {@link List} of records
 *  and can {@code create, add, remove, search, sorting} for these objects.
 */
public interface WorkRecordBook {

    /**
     * The day of month, for {@linkplain WorkRecordBook#sorting()} method, is used like a border,
     *  when sorting {@link IDentalWork}s by month.
     */
    byte PAY_DAY = 10;

    /**
     * Create a {@link IDentalWork} object and add it to {@link List} field of this instance.
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
    IDentalWork createRecord(String patient, String clinic, String product, int quantity, LocalDate complete) throws WorkRecordBookException;

    /**
     * Create a {@link IDentalWork} object and add it to {@link List} field of this instance.
     * @param patient the patient name or surname of this work.
     * @param clinic the clinic of this work.
     * @return The created DentalWork object.
     */
    IDentalWork createRecord(String patient, String clinic);

    /**
     * Create {@link edu.dental.domain.entities.Product product} object and add it into the given {@link IDentalWork}.
     * @param dentalWork the {@link IDentalWork} object to adding a {@link edu.dental.domain.entities.Product product}.
     * @param product the product type title to add (should be containing in {@link Map}).
     * @param quantity the quantity of a product items.
     * @return the modified given DentalWork object back.
     * @throws WorkRecordBookException if a given arguments is incorrect or the given product type
     *  is not contain in {@link Map}.
     */
    IDentalWork addProductToRecord(IDentalWork dentalWork, String product, int quantity) throws WorkRecordBookException;

    /**
     * Remove {@link edu.dental.domain.entities.Product product} object from the given {@linkplain  IDentalWork#getProducts() record}.
     * @param dentalWork the {@link IDentalWork} object to remove a {@link edu.dental.domain.entities.Product product}.
     * @param product the product type title to remove (should be containing in {@link Map}).
     * @return the modified given DentalWork object back.
     * @throws WorkRecordBookException if a given arguments is null.
     */
    IDentalWork removeProduct(IDentalWork dentalWork, String product) throws WorkRecordBookException;

    /**
     * Delete {@link IDentalWork} object from the instance {@link List} field.
     * @param dentalWork the object to delete.
     * @return true if all successful.
     */
    boolean deleteRecord(IDentalWork dentalWork);

    /**
     * Search a {@link IDentalWork} by {@code patient and clinic} fields.
     * @param patient the patient name or surname of required record.
     * @param clinic the clinic  of required record.
     * @return the required DentalWork object.
     * @throws WorkRecordBookException if a given argument is incorrect or null, and if such object is not found.
     */
    IDentalWork searchRecord(String patient, String clinic) throws WorkRecordBookException;

    /**
     * Search a {@link DentalWork} by {@code id} fields.
     * @param id the id of required record.
     * @return the required {@link DentalWork} object.
     * @throws WorkRecordBookException if a given argument is incorrect or null, and if such object is not found.
     */
    IDentalWork getByID(int id) throws WorkRecordBookException;

    /**
     * Sort {@link IDentalWork} objects in the instance {@link List} field by {@linkplain IDentalWork#getStatus()}
     *  and {@linkplain IDentalWork#getComplete()} values. If record is suited, it's set {@code closed}, removing from the instance list
     *  and add into new {@link List list}.
     * @return the {@link List} object of sorted closed records.
     */
    List<IDentalWork> sorting();

    /**
     * Get the {@link List list} field of {@link DentalWork} objects.
     * @return {@link List} object, containing {@link DentalWork records}.
     */
    List<IDentalWork> getList();

    /**
     * Get the {@link ProductMap} field of {@link edu.dental.domain.entities.Product products}.
     * @return {@link ProductMap} object, containing {@link edu.dental.domain.entities.Product product}
     *  {@link edu.dental.domain.records.ProductMap.Item entries}.
     */
    ProductMap getMap();
}
