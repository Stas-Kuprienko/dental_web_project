package dental.app.records;

import dental.app.Account;

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



}
