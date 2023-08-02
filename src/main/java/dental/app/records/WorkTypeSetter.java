package dental.app.records;

import java.util.HashMap;

public class WorkTypeSetter {

    /**
     * The map of the work types and prices.
     */
    private final HashMap<String, Integer> workTypes;

    WorkTypeSetter() {
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
     * Remove the type of work from the {@linkplain WorkTypeSetter#workTypes works} by a {@linkplain java.util.HashMap#get(Object) key}.
     * @param title The title of the work type as a Key
     * @return True if the Key of the work type removed
     * or false if no such element
     */
    public boolean removeWorkType(String title) {
        //TODO
        return workTypes.containsValue
                (workTypes.remove(title));
    }


    HashMap<String, Integer> getWorkTypes() {
        return workTypes;
    }


    class WorkInRecordSetter {

        private RecordItem record;
        private Work workType;

        public WorkInRecordSetter(RecordItem record, Work workType) {
            this.record = record;
            this.workType = workType;
        }

    }
}
