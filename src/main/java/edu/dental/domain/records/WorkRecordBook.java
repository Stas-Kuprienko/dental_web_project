package edu.dental.domain.records;

import edu.dental.domain.entities.DentalWork;
import edu.dental.domain.entities.I_DentalWork;

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
     *  when sorting {@link I_DentalWork}s by month.
     */
    byte PAY_DAY = 10;

    /**
     * Create a {@link I_DentalWork} object and add it to {@link List} field of this instance.
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
    I_DentalWork createRecord(String patient, String clinic, String product, int quantity, LocalDate complete) throws WorkRecordBookException;

    /**
     * Create a {@link I_DentalWork} object and add it to {@link List} field of this instance.
     * @param patient the patient name or surname of this work.
     * @param clinic the clinic of this work.
     * @return The created DentalWork object.
     */
    I_DentalWork createRecord(String patient, String clinic);

    /**
     * Create {@link edu.dental.domain.entities.Product product} object and add it into the given {@link I_DentalWork}.
     * @param dentalWork the {@link I_DentalWork} object to adding a {@link edu.dental.domain.entities.Product product}.
     * @param product the product type title to add (should be containing in {@link Map}).
     * @param quantity the quantity of a product items.
     * @return the modified given DentalWork object back.
     * @throws WorkRecordBookException if a given arguments is incorrect or the given product type
     *  is not contain in {@link Map}.
     */
    I_DentalWork addProductToRecord(I_DentalWork dentalWork, String product, int quantity) throws WorkRecordBookException;

    /**
     * Remove {@link edu.dental.domain.entities.Product product} object from the given {@linkplain  I_DentalWork#getProducts() record}.
     * @param dentalWork the {@link I_DentalWork} object to remove a {@link edu.dental.domain.entities.Product product}.
     * @param product the product type title to remove (should be containing in {@link Map}).
     * @return the modified given DentalWork object back.
     * @throws WorkRecordBookException if a given arguments is null.
     */
    I_DentalWork removeProduct(I_DentalWork dentalWork, String product) throws WorkRecordBookException;

    /**
     * Delete {@link I_DentalWork} object from the instance {@link List} field.
     * @param dentalWork the object to delete.
     * @return true if all successful.
     */
    boolean deleteRecord(I_DentalWork dentalWork);

    /**
     * Search a {@link I_DentalWork} by {@code patient and clinic} fields.
     * @param patient the patient name or surname of required record.
     * @param clinic the clinic  of required record.
     * @return the required DentalWork object.
     * @throws WorkRecordBookException if a given argument is incorrect or null, and if such object is not found.
     */
    I_DentalWork searchRecord(String patient, String clinic) throws WorkRecordBookException;

    /**
     * Search a {@link DentalWork} by {@code id} fields.
     * @param id the id of required record.
     * @return the required {@link DentalWork} object.
     * @throws WorkRecordBookException if a given argument is incorrect or null, and if such object is not found.
     */
    I_DentalWork getByID(int id) throws WorkRecordBookException;

    /**
     * Sort {@link I_DentalWork} objects in the instance {@link List} field by {@linkplain I_DentalWork#getStatus()}
     *  and {@linkplain I_DentalWork#getComplete()} values. If record is suited, it's set {@code closed}, removing from the instance list
     *  and add into new {@link List list}.
     * @return the {@link List} object of sorted closed records.
     */
    List<I_DentalWork> sorting();

    /**
     * Get the {@link List list} field of {@link DentalWork} objects.
     * @return {@link List} object, containing {@link DentalWork records}.
     */
    List<I_DentalWork> getList();

    /**
     * Get the {@link ProductMap} field of {@link edu.dental.domain.entities.Product products}.
     * @return {@link ProductMap} object, containing {@link edu.dental.domain.entities.Product product}
     *  {@link edu.dental.domain.records.ProductMap.Item entries}.
     */
    ProductMap getMap();
}
