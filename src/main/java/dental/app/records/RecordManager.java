package dental.app.records;

import dental.app.Account;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;

public class RecordManager {

    /**
     * The {@link ArrayList list} of {@link RecordItem} objects for account.
     */
    private final ArrayList<RecordItem> records;

    /**
     * The object for manipulating {@link Work workTypes}.
     */
    private final WorkTypeSetter workTypeSetter;


    public RecordManager() {
        this.records = new ArrayList<>();
        this.workTypeSetter = new WorkTypeSetter();
    }

    /**
     * Create a new {@link RecordItem} object and add it in user's list.
     * @param patient  The patient name/surname.
     * @param clinic   The clinic title.
     * @param complete The completion {@link LocalDate date}.
     * @return  {@link RecordItem} object.
     */
    public RecordItem createRecord(String patient, String clinic, LocalDate complete) {
        if ((patient == null||patient.isEmpty())||(clinic == null||clinic.isEmpty())||(complete == null)) {
            return null;
        }
        RecordItem record = new RecordItem(patient, clinic, complete);
        //TODO
        this.records.add(record);
        return record;
    }

    /**
     * Create a new {@link RecordItem} object and add it in user's list.
     * @param patient  The patient name/surname.
     * @param clinic   The clinic title.
     * @param workType The title of the work type.
     * @param quantity The quantity of the work items.
     * @param complete The completion {@link LocalDate date}.
     * @return  {@link RecordItem} object.
     */
    public RecordItem createRecord(String patient, String clinic, String workType, byte quantity, LocalDate complete) {
        if ((patient == null||patient.isEmpty())||(clinic == null||clinic.isEmpty())||(workType == null||workType.isEmpty())) {
            return null;
        }
        Work work;
        try {
            work = workTypeSetter.createWorkObject(workType, quantity);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            return null;
        }
        RecordItem record = new RecordItem(patient, clinic, work, complete);
        this.records.add(record);
        return record;
    }

    /**
     * Add a new {@link Work} object in works list of the {@link RecordItem record}.
     * @param record   The record to add.
     * @param title    The title of the work to add.
     * @param quantity The quantity of the work items.
     * @return  True if it was successful.
     */
    public boolean addWorkInRecord(RecordItem record, String title, byte quantity) {
        Work work;
        try {
            work = workTypeSetter.createWorkObject(title, quantity);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            return false;
        }
        return record.getWorks().add(work);
    }

    /**
     * Edit a {@link Work} values in the {@link RecordItem} object.
     * @param record   The {@link RecordItem} object that needed to edit.
     * @param title    The work type title to edit.
     * @param quantity The quantity of work items.
     * @return True if it was successful.
     */
    public boolean editWorkInRecord(RecordItem record, String title, byte quantity) {
        //TODO
        if (removeWorkOfRecord(record, title)) {
            return addWorkInRecord(record, title, quantity);
        }
        return false;
    }

    public boolean removeWorkOfRecord(RecordItem record, String title) {
        for (Work w : record.getWorks()) {
            if (w.getTitle().equalsIgnoreCase(title)) {
                return record.getWorks().remove(w);
            }
        }
        return false;
    }

    /**
     * Search a record in {@linkplain RecordManager#records records list}
     *  by patient and clinic.
     * @param name    The patient name of the wanted record.
     * @param clinic  The wanted clinic title.
     * @return The found {@link RecordItem record} object, if any, or null.
     */
    public RecordItem searchOpenRecords(String name, String clinic) {
        for (RecordItem r : this.records) {
            if (r.getPatient().equalsIgnoreCase(name) && (r.getClinic().equalsIgnoreCase(clinic))) {
                return r;
            }
        }
        return null;
    }

    public ArrayList<RecordItem> searchArchiveRecord(Account account, String name) {

        //TODO

        return null;
    }


    public ArrayList<RecordItem> getRecords() {
        return records;
    }

    public HashMap<String, Integer> getWorkTypes() {
        return workTypeSetter.getWorkTypes();
    }
}
