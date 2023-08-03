package dental.app.records;

import dental.app.Account;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;

public class RecordManager {

    /**
     * The {@link ArrayList list} of {@link Record} objects for account.
     */
    private final ArrayList<Record> records;

    /**
     * The {@link HashMap map} of the work types and prices.
     */
    private final HashMap<String, Integer> workTypes;


    public RecordManager() {
        this.records = new ArrayList<>();
        this.workTypes = new HashMap<>();
    }


    /**
     * Enter the type of work in the HashMap.
     * @param title The title of the work type.
     * @param price The price of the work type.
     */
    public void putWorkType(String title, int price) {
        if ((title == null || title.isEmpty()) || (price < 1)) {
            return;
        }
        workTypes.put(title, price);
    }

    /**
     * Make a new {@link Work} object for entry a record.
     * @param title  The title of the work type.
     * @param quantity The quantity of the work items.
     * @return The {@link Work} object.
     */
    public Work createWorkObject(String title, byte quantity)
            throws IllegalArgumentException {
        if (((title == null) || title.isEmpty())) {
            throw new IllegalArgumentException();
        } else {
            return new Work(title, quantity, workTypes.get(title));
        }
    }

    /**
     * Remove the type of work from the {@linkplain RecordManager#workTypes works}
     *  by a {@linkplain java.util.HashMap#get(Object) key}.
     * @param title The title of the work type as a Key
     * @return True if the Key of the work type removed
     * or false if no such element
     */
    public boolean removeWorkType(String title) {
        return workTypes.remove(title) != null;
    }

    /**
     * Create a new {@link Record} object and add it in user's list.
     * @param patient  The patient name/surname.
     * @param clinic   The clinic title.
     * @param complete The completion {@link LocalDate date}.
     * @return  {@link Record} object.
     */
    public Record createRecord(String patient, String clinic, LocalDate complete) {
        if ((patient == null||patient.isEmpty())||(clinic == null||clinic.isEmpty())||(complete == null)) {
            return null;
        }
        Record record = new Record(patient, clinic, complete);
        //TODO
        this.records.add(record);
        return record;
    }

    /**
     * Create a new {@link Record} object and add it in user's list.
     * @param patient  The patient name/surname.
     * @param clinic   The clinic title.
     * @param workType The title of the work type.
     * @param quantity The quantity of the work items.
     * @param complete The completion {@link LocalDate date}.
     * @return  {@link Record} object.
     */
    public Record createRecord(String patient, String clinic, String workType, byte quantity, LocalDate complete) {
        if ((patient == null||patient.isEmpty())||(clinic == null||clinic.isEmpty())||(workType == null||workType.isEmpty())) {
            return null;
        }
        Work work;
        try {
            work = createWorkObject(workType, quantity);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            return null;
        }
        Record record = new Record(patient, clinic, work, complete);
        this.records.add(record);
        return record;
    }

    /**
     * Add a new {@link Work} object in works list of the {@link Record record}.
     * @param record   The record to add.
     * @param title    The title of the work to add.
     * @param quantity The quantity of the work items.
     * @return  True if it was successful.
     */
    public boolean addWorkInRecord(Record record, String title, byte quantity) {
        Work work;
        try {
            work = createWorkObject(title, quantity);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            return false;
        }
        return record.getWorks().add(work);
    }

    /**
     * Edit a {@link Work} values in the {@link Record} object.
     * @param record   The {@link Record} object that needed to edit.
     * @param title    The work type title to edit.
     * @param quantity The quantity of work items.
     * @return True if it was successful.
     */
    public boolean editWorkInRecord(Record record, String title, byte quantity) {
        //TODO
        if (removeWorkOfRecord(record, title)) {
            return addWorkInRecord(record, title, quantity);
        }
        return false;
    }

    public boolean removeWorkOfRecord(Record record, String title) {
        for (Work w : record.getWorks()) {
            if (w.title().equalsIgnoreCase(title)) {
                return record.getWorks().remove(w);
            }
        }
        return false;
    }


    public ArrayList<Record> getRecords() {
        return this.records;
    }

    public HashMap<String, Integer> getWorkTypes() {
        return this.workTypes;
    }
}
