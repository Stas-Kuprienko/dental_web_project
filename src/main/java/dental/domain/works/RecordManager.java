package dental.domain.works;

import dental.domain.data_structures.MyList;

import java.time.LocalDate;

public final class RecordManager {

    /**
     * The {@link MyList list} of the unclosed {@link WorkRecord} objects for account.
     */
    public final MyList<WorkRecord> workRecords;

    public final ProductMapper productMap;


    public RecordManager() {
        this.workRecords = new MyList<>();
        this.productMap = new ProductMapper();
    }

    /**
     * Create a new {@link WorkRecord} object and add it in user's list.
     *
     * @param patient  The patient name/surname.
     * @param clinic   The clinic title.
     * @param complete The completion {@link LocalDate date}.
     * @return {@link WorkRecord} object.
     */
    public WorkRecord createRecord(String patient, String clinic, LocalDate complete) {

        WorkRecord workRecord = WorkRecord.create().setPatient(patient).setClinic(clinic).setComplete(complete).build();

        this.workRecords.add(workRecord);

        return workRecord;
    }

    /**
     * Add a new {@link Product} object in works list of the {@link WorkRecord workRecord}.
     *
     * @param workRecord The workRecord to add.
     * @param title      The title of the product to add.
     * @param quantity   The quantity of the product items.
     */
    public void addProductInWork(WorkRecord workRecord, String title, int quantity) {
        Product product = productMap.buildProduct(title, quantity);
        workRecord.getProducts().add(product);
    }

    /**
     * Edit quantity of the {@link Product} object in the {@link WorkRecord}.
     *
     * @param workRecord The {@link WorkRecord} object to edit.
     * @param title      The title of the product to edit.
     * @param quantity   New quantity value.
     * @return True if the edit was successful.
     */
    public boolean editProductQuantity(WorkRecord workRecord, String title, int quantity) {
        removeProduct(workRecord, title);
        return workRecord.getProducts().add(productMap.buildProduct(title, quantity));
    }

    /**
     * Remove the {@link Product} object in the {@link WorkRecord}.
     *
     * @param workRecord The {@link WorkRecord} objects to do.
     * @param title      The title of the product to remove.
     * @return True if it was successful.
     */
    public boolean removeProduct(WorkRecord workRecord, String title) {
        Product p = findProduct(workRecord, title);
        return p != null && workRecord.getProducts().remove(p);
    }

    public Product findProduct(WorkRecord workRecord, String title) {
        title = title.toLowerCase();
        for (Product p : workRecord.getProducts()) {
            if (p.title().equals(title)) {
                return p;
            }
        }
        return null;
    }

    public void categorizeWorkRecords() {
        //TODO
    }
}
