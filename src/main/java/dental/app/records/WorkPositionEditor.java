package dental.app.records;

import dental.app.userset.Account;

class WorkPositionEditor {

    /**
     * The {@link Record} object to manipulating.
     */
    private final Record record;

    /**
     * The current user {@link Account account} {@link RecordManager} object.
     */
    private final RecordManager recordManager;

    /**
     * Create WorkTool object.
     * @param recordManager The current user {@link Account account} {@link RecordManager} object.
     * @param record        The {@link Record} object to manipulating.
     */
    WorkPositionEditor(RecordManager recordManager, Record record) {
        this.recordManager = recordManager;
        this.record = record;
    }

    /**
     * Edit quantity of the {@link Work} object in the {@link Record}.
     * @param title    The title of the work type to edit.
     * @param quantity New quantity value.
     * @return True if the edit was successful.
     */
    boolean editWorkQuantity(String title, byte quantity) {
        if (!(removeWork(title))) {
            return false;
        }
        return recordManager.addWorkPosition(record, title, quantity);
    }

    /**
     * Remove the {@link Work} object in the {@link Record}.
     * @param title  The title of the work type to remove.
     * @return True if it was successful.
     */
    boolean removeWork(String title) {
        return record.getWorks().remove(getWork(title));
    }

    /**
     * Find the {@link Work} object in {@link Record} by title.
     * @param title The title String of the required work position.
     * @return The required {@link Work} object, if such exists, or null.
     */
    private Work getWork(String title) {
        for (Work w : record.getWorks()) {
            if (w.title().equalsIgnoreCase(title)) {
                return w;
            }
        }
        return null;
    }

    public Record getRecord() {
        return record;
    }
}