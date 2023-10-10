package edu.dental.domain.records;

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
     *  when sorting {@link WorkRecord}s by month.
     */
    byte PAY_DAY = 10;

    /**
     * Create a {@link WorkRecord} object and add it to {@link Collection} field of this instance.
     * @param patient the patient name or surname of this work.
     * @param clinic the clinic of this work.
     * @param product the {@link edu.dental.domain.entities.Product product} type title
     *                 (that having in {@link Mapper}).
     * @param quantity the quantity of product items (no more than 32, depending on the type of product).
     * @param complete The date, when this work should be completed.
     * @return The created {@link WorkRecord} object.
     * @throws WorkRecordBookException if a given arguments is incorrect or the given product type
     *  is not contain in {@link Mapper}.
     */
    WorkRecord createRecord(String patient, String clinic, String product, int quantity, String complete) throws WorkRecordBookException;

    /**
     * Create a {@link WorkRecord} object and add it to {@link Collection} field of this instance.
     * @param patient the patient name or surname of this work.
     * @param clinic the clinic of this work.
     * @param complete The date, when this work should be completed.
     * @return The created {@link WorkRecord} object.
     * @throws WorkRecordBookException if the given arguments is incorrect.
     */
    WorkRecord createRecord(String patient, String clinic, String complete) throws WorkRecordBookException;

    /**
     * Create {@link edu.dental.domain.entities.Product product} object and add it into the given {@link WorkRecord}.
     * @param workRecord the {@link WorkRecord} object to adding a {@link edu.dental.domain.entities.Product product}.
     * @param product the product type title to add (should be containing in {@link Mapper}).
     * @param quantity the quantity of a product items.
     * @return the modified given {@link WorkRecord} object back.
     * @throws WorkRecordBookException if a given arguments is incorrect or the given product type
     *  is not contain in {@link Mapper}.
     */
    WorkRecord addProductToRecord(WorkRecord workRecord, String product, int quantity) throws WorkRecordBookException;

    /**
     * Remove {@link edu.dental.domain.entities.Product product} object from the given {@linkplain  WorkRecord#getProducts() record}.
     * @param workRecord the {@link WorkRecord} object to remove a {@link edu.dental.domain.entities.Product product}.
     * @param product the product type title to remove (should be containing in {@link Mapper}).
     * @return the modified given {@link WorkRecord} object back.
     * @throws WorkRecordBookException if a given arguments is null.
     */
    WorkRecord removeProduct(WorkRecord workRecord, String product) throws WorkRecordBookException;

    /**
     * Delete {@link WorkRecord} object from the instance {@link Collection} field.
     * @param workRecord the object to delete.
     * @return true if all successful.
     */
    boolean deleteRecord(WorkRecord workRecord);

    /**
     * Search a {@link WorkRecord} by {@code patient and clinic} fields.
     * @param patient the patient name or surname of required record.
     * @param clinic the clinic  of required record.
     * @return the required {@link WorkRecord} object.
     * @throws WorkRecordBookException if a given argument is incorrect or null, and if such object is not found.
     */
    WorkRecord searchRecord(String patient, String clinic) throws WorkRecordBookException;

    /**
     * Search a {@link WorkRecord} by {@code id} fields.
     * @param id the id of required record.
     * @return the required {@link WorkRecord} object.
     * @throws WorkRecordBookException if a given argument is incorrect or null, and if such object is not found.
     */
    WorkRecord getByID(int id) throws WorkRecordBookException;

    /**
     * Sort {@link WorkRecord} objects in the instance {@link Collection} field by {@linkplain WorkRecord#isClosed()}
     *  and {@linkplain WorkRecord#getComplete()} values. If record is suited, it's set {@code closed}, removing from the instance list
     *  and add into new {@link Collection list}.
     * @return the {@link Collection} object of sorted closed records.
     */
    Collection<WorkRecord> sorting();

    /**
     * Get the {@link Collection list} field of {@link WorkRecord} objects.
     * @return {@link Collection} object, containing {@link WorkRecord records}.
     */
    Collection<WorkRecord> getList();

    /**
     * Get the {@link Map} field of {@link edu.dental.domain.entities.Product products}.
     * @return {@link Map} object, containing {@link edu.dental.domain.entities.Product product}
     *  {@link java.util.Map.Entry entries}.
     */
    Map<String, Integer> getMap();
}
