package dental.app.records;

import java.util.HashMap;

class WorkTypeTool {

    WorkTypeTool() {
        this.workTypes = new HashMap<>();
    }

    /**
     * The {@link HashMap map} of the work types and prices.
     */
    private final HashMap<String, Integer> workTypes;


    /**
     * Create a new {@link Work} object for entry a record.
     * @param title  The title of the work type.
     * @param quantity The quantity of the work items.
     * @return The {@link Work} object.
     */
    protected Work createWorkObject(String title, byte quantity) {
        if (((title == null) || title.isEmpty())) {
            return null;
        } else {
            return new Work(title, quantity, workTypes.get(title));
        }
    }

    /**
     * Enter the type of work in the HashMap.
     * @param title The title of the work type.
     * @param price The price of the work type.
     */
    public void addWorkType(String title, int price) {
        if ((title == null || title.isEmpty()) || (price < 1)) {
            return;
        }
        workTypes.put(title, price);
    }

    /**
     * Remove the type of work from the {@linkplain WorkTypeTool#workTypes works}
     *  by a {@linkplain java.util.HashMap#get(Object) key}.
     * @param title The title of the work type as a Key
     * @return True if the Key of the work type removed
     * or false if no such element
     */
    public boolean removeWorkType(String title) {
        return workTypes.remove(title) != null;
    }



    public HashMap<String, Integer> getWorkTypes() {
        return workTypes;
    }
}
