package dental.app.records;

import java.util.ArrayList;

public class Searcher {

    private final RecordManager recordManager;

    /**
     * The {@link ArrayList list} of a found records.
     */
    ArrayList<Record> relevant;

    /**
     * Create the {@link Searcher} objects.
     * @param recordManager The {@link RecordManager} objects of a user account.
     */
    public Searcher(RecordManager recordManager) {
        this.recordManager = recordManager;
        this.relevant = new ArrayList<>();
    }

    //TODO

    /**
     * Search a {@link Record records} in {@linkplain RecordManager#getRecords() records list}
     *  by patient and add them to {@link Searcher#relevant}.
     * @param patient    The patient name of the wanted record.
     */
    public void search(String patient) {
        for (Record r : recordManager.getRecords()) {
            if (r.getPatient().equalsIgnoreCase(patient)) {
                relevant.add(r);
            }
        }
    }

    /**
     * Search a {@link Record records} in {@linkplain RecordManager#getRecords() records list}
     *  by patient and clinic then add them to {@link Searcher#relevant}.
     * @param patient    The patient name of the wanted record.
     * @param clinic  The wanted clinic title.
     */
    public void search(String patient, String clinic) {
        for (Record r : recordManager.getRecords()) {
            if ((r.getPatient().equalsIgnoreCase(patient))
                    && (r.getClinic().equalsIgnoreCase(clinic))) {
                relevant.add(r);
            }
        }
    }
}