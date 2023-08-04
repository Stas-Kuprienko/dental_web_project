package dental.app.records;

class WorkTool {

    /**
     * The {@link Record} object to manipulating.
     */
    private final Record record;

    /**
     * The current user {@link dental.app.Account account} {@link RecordManager} object.
     */
    private final RecordManager recordManager;

    /**
     * Create WorkTool object.
     * @param recordManager The current user {@link dental.app.Account account} {@link RecordManager} object.
     * @param record        The {@link Record} object to manipulating.
     */
    WorkTool(RecordManager recordManager, Record record) {
        this.recordManager = recordManager;
        this.record = record;
    }

    /**
     *
     * @param title
     * @param quantity
     * @return
     */
    boolean editWorkQuantity(String title, byte quantity) {
        if (!(removeWork(title))) {
            return false;
        }
        return recordManager.addWorkInRecord(record, title, quantity);
    }

    /**
     *
     * @param title
     * @return
     */
    boolean removeWork(String title) {
        return record.getWorks().remove(getWork(title));
    }

    /**
     *
     * @param title
     * @return
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