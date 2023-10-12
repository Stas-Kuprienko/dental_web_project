package edu.dental.domain.records;

import edu.dental.domain.entities.I_WorkRecord;
import edu.dental.domain.entities.WorkRecord;

import java.util.Collection;
import java.util.Map;

/**
 * The {@code WorkRecordBook} represents object is used for managing {@link WorkRecord records}.
 * The implementation must contain a field - {@link Collection} of records
 *  and can {@code create, add, remove, search, sorting} for these objects.
 */
public interface WorkRecordBook {

    /**
     * The day of month, for {@linkplain WorkRecordBook#sorting()} method, is used like a border,
     *  when sorting {@link I_WorkRecord}s by month.
     */
    byte PAY_DAY = 10;

    /**
     * Create a {@link I_WorkRecord} object and add it to {@link Collection} field of this instance.
     * @param patient the patient name or surname of this work.
     * @param clinic the clinic of this work.
     * @param product the {@link edu.dental.domain.entities.Product product} type title
     *                 (that having in {@link Map}).
     * @param quantity the quantity of product items (no more than 32, depending on the type of product).
     * @param complete The date, when this work should be completed.
     * @return The created WorkRecord object.
     * @throws WorkRecordBookException if a given arguments is incorrect or the given product type
     *  is not contain in {@link Map}.
     */
    I_WorkRecord createRecord(String patient, String clinic, String product, int quantity, String complete) throws WorkRecordBookException;

    /**
     * Create a {@link I_WorkRecord} object and add it to {@link Collection} field of this instance.
     * @param patient the patient name or surname of this work.
     * @param clinic the clinic of this work.
     * @param complete The date, when this work should be completed.
     * @return The created WorkRecord object.
     * @throws WorkRecordBookException if the given arguments is incorrect.
     */
    I_WorkRecord createRecord(String patient, String clinic, String complete) throws WorkRecordBookException;

    /**
     * Create {@link edu.dental.domain.entities.Product product} object and add it into the given {@link I_WorkRecord}.
     * @param workRecord the {@link I_WorkRecord} object to adding a {@link edu.dental.domain.entities.Product product}.
     * @param product the product type title to add (should be containing in {@link Map}).
     * @param quantity the quantity of a product items.
     * @return the modified given WorkRecord object back.
     * @throws WorkRecordBookException if a given arguments is incorrect or the given product type
     *  is not contain in {@link Map}.
     */
    I_WorkRecord addProductToRecord(I_WorkRecord workRecord, String product, int quantity) throws WorkRecordBookException;

    /**
     * Remove {@link edu.dental.domain.entities.Product product} object from the given {@linkplain  I_WorkRecord#getProducts() record}.
     * @param workRecord the {@link I_WorkRecord} object to remove a {@link edu.dental.domain.entities.Product product}.
     * @param product the product type title to remove (should be containing in {@link Map}).
     * @return the modified given WorkRecord object back.
     * @throws WorkRecordBookException if a given arguments is null.
     */
    I_WorkRecord removeProduct(I_WorkRecord workRecord, String product) throws WorkRecordBookException;

    /**
     * Delete {@link I_WorkRecord} object from the instance {@link Collection} field.
     * @param workRecord the object to delete.
     * @return true if all successful.
     */
    boolean deleteRecord(I_WorkRecord workRecord);

    /**
     * Search a {@link I_WorkRecord} by {@code patient and clinic} fields.
     * @param patient the patient name or surname of required record.
     * @param clinic the clinic  of required record.
     * @return the required WorkRecord object.
     * @throws WorkRecordBookException if a given argument is incorrect or null, and if such object is not found.
     */
    I_WorkRecord searchRecord(String patient, String clinic) throws WorkRecordBookException;

    /**
     * Search a {@link WorkRecord} by {@code id} fields.
     * @param id the id of required record.
     * @return the required {@link WorkRecord} object.
     * @throws WorkRecordBookException if a given argument is incorrect or null, and if such object is not found.
     */
    I_WorkRecord getByID(int id) throws WorkRecordBookException;

    /**
     * Sort {@link I_WorkRecord} objects in the instance {@link Collection} field by {@linkplain I_WorkRecord#getStatus()}
     *  and {@linkplain I_WorkRecord#getComplete()} values. If record is suited, it's set {@code closed}, removing from the instance list
     *  and add into new {@link Collection list}.
     * @return the {@link Collection} object of sorted closed records.
     */
    Collection<I_WorkRecord> sorting();

    /**
     * Get the {@link Collection list} field of {@link WorkRecord} objects.
     * @return {@link Collection} object, containing {@link WorkRecord records}.
     */
    Collection<I_WorkRecord> getList();

    /**
     * Get the {@link java.util.Map} field of {@link edu.dental.domain.entities.Product products}.
     * @return {@link java.util.Map} object, containing {@link edu.dental.domain.entities.Product product}
     *  {@link java.util.Map.Entry entries}.
     */
    java.util.Map<String, Integer> getMap();
}
