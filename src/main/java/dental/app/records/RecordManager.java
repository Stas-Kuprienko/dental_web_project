package dental.app.records;

import dental.app.MyList;

import java.time.LocalDate;
import java.util.HashMap;

public final class RecordManager {

    /**
     * The {@link MyList list} of the unclosed {@link Record} objects for account.
     */
    public final MyList<Record> records;

    public final WorkTypeTool workTool;


    public RecordManager() {
        this.records = new MyList<>();
        this.workTool = new WorkTypeTool();
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
    public Record createRecord(String patient, String clinic, String workType, int quantity, LocalDate complete) {
        if ((patient == null||patient.isEmpty())||(clinic == null||clinic.isEmpty())||(workType == null||workType.isEmpty())) {
            return null;
        }
        Work work = workTool.createWorkObject(workType, (byte) quantity);
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
    public boolean addWorkPosition(Record record, String title, int quantity) {
        Work work = workTool.createWorkObject(title, (byte) quantity);
        if (work == null) {
            return false;
        }
        return record.getWorks().add(work);
    }

    /**
     * Edit quantity of the {@link Work} object in the {@link Record}.
     * @param record   The {@link Record} object to edit.
     * @param title    The title of the work type to edit.
     * @param quantity New quantity value.
     * @return True if the edit was successful.
     */
    public boolean editWorkQuantity(Record record, String title, int quantity) {
        removeWorkPosition(record, title);
        return record.getWorks().add(workTool.createWorkObject(title, quantity));
    }

    /**
     * Remove the {@link Work} object in the {@link Record}.
     * @param record The {@link Record} objects to do.
     * @param title  The title of the work type to remove.
     * @return True if it was successful.
     */
    public boolean removeWorkPosition(Record record, String title) {
        return record.getWorks().remove(workTool.getWork(record, title));
    }


    public HashMap<String, Integer> getWorkTypes() {
        return workTool.getWorkTypes();
    }
}
