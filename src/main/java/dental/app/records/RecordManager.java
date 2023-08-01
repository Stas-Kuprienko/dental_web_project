package dental.app.records;

import dental.app.Account;

import java.time.LocalDate;
import java.util.ArrayList;

public class RecordManager {

    private RecordManager() {}

    /**
     * Enter the type of work in the HashMap.
     * @param title The title of the work type.
     * @param price The price of the work type.
     */
    public static void putWorkType(Account account, String title, int price) {
        account.getWorkTypes().put(title, price);
    }

    /**
     * Make a new {@link Work} object for entry a record.
     * @param account The {@link Account} object that requires.
     * @param title  The title of the work type.
     * @param quantity The quantity of the work items.
     * @return The {@link Work} object.
     */
    public static Work createWork(Account account, String title, byte quantity)
                                                throws IllegalArgumentException {
        if ((account == null) || ((title == null) || title.isEmpty())) {
            throw new IllegalArgumentException();
        } else {
            return new Work(title, quantity, account.getWorkTypes().get(title));
        }
    }

    /**
     * Remove the type of work from the {@linkplain Account#getWorkTypes() works} by a {@linkplain java.util.HashMap#get(Object) key}.
     * @param title The title of the work type as a Key
     * @return True if the Key of the work type removed
     * or false if no such element
     */
    public boolean removeWorkType(Account account, String title) {
        //TODO
        return account.getWorkTypes().containsValue
               (account.getWorkTypes().remove(title));
    }

    /**
     * Create a new {@link RecordItem} object and add it in user's list.
     * @param account The user {@link Account} object.
     * @param patient  The patient name/surname.
     * @param clinic   The clinic title.
     * @param works    The {@link Work works} array.
     * @param complete The completion {@link LocalDate date}.
     * @return  {@link RecordItem} object.
     */
    public static RecordItem createRecord(Account account, String patient, String clinic, ArrayList<Work> works, LocalDate complete) {
        if ((patient == null||patient.isEmpty())||(clinic == null||clinic.isEmpty())||(complete == null)) {
            return null;
        }
        RecordItem record = new RecordItem(patient, clinic, works, complete);
        account.getRecords().add(record);
        return record;
    }

    /**
     * Create a new {@link RecordItem} object and add it in user's list.
     * @param account The user {@link Account} object.
     * @param patient  The patient name/surname.
     * @param clinic   The clinic title.
     * @param work    The {@link Work} object.
     * @param complete The completion {@link LocalDate date}.
     * @return  {@link RecordItem} object.
     */
    public static RecordItem createRecord(Account account, String patient, String clinic, Work work, LocalDate complete) {
        if ((patient == null||patient.isEmpty())||(clinic == null||clinic.isEmpty())||(complete == null)||(work == null)) {
            return null;
        }
        RecordItem record = new RecordItem(patient, clinic, work, complete);
        account.getRecords().add(record);
        return record;
    }

    /**
     * Add a new {@link Work} object in works list of the {@link RecordItem record}.
     * @param account  The user {@link Account} object.
     * @param record   The record to add.
     * @param title    The title of the work to add.
     * @param quantity The quantity of the work items.
     * @return  True if it was successful.
     */
    public static boolean addWorkInRecord(Account account, RecordItem record, String title, byte quantity) {
        Work work;
        try {
            work = createWork(account, title, quantity);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            return false;
        }
        return record.getWorks().add(work);
    }

    /**
     * Edit a {@link Work} values in the {@link RecordItem} object.
     * @param account  The user {@link Account} object.
     * @param record   The {@link RecordItem} object that needed to edit.
     * @param title    The work type title to edit.
     * @param quantity The quantity of work items.
     * @return True if it was successful.
     */
    public static boolean editWorkInRecord(Account account, RecordItem record, String title, byte quantity) {
        //TODO
        if (removeWorkOfRecord(record, title)) {
            return addWorkInRecord(account, record, title, quantity);
        }
        return false;
    }

    public static boolean removeWorkOfRecord(RecordItem record, String title) {
        for (Work w : record.getWorks()) {
            if (w.getTitle().equalsIgnoreCase(title)) {
                return record.getWorks().remove(w);
            }
        }
        return false;
    }

    /**
     * Search an uncompleted record in {@linkplain Account#getRecords() records list}
     *  by patient and clinic.
     * @param account The user {@link Account} object.
     * @param name    The patient name of the wanted record.
     * @param clinic  The wanted clinic title.
     * @return The found {@link RecordItem record} object, if any, or null.
     */
    public static RecordItem searchOpenRecords(Account account, String name, String clinic) {
        for (RecordItem r : account.getRecords()) {
            if (r.getPatient().equalsIgnoreCase(name) && (r.getClinic().equalsIgnoreCase(clinic))) {
                return r;
            }
        }
        return null;
    }

    public static ArrayList<RecordItem> searchArchiveRecord(Account account, String name) {

        //TODO

        return null;
    }

}
