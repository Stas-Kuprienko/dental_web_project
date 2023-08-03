package dental.app.records;

import java.util.ArrayList;

public class Searcher {

    private final RecordManager recordManager;
    ArrayList<Record> relevant;

    public Searcher(RecordManager recordManager) {
        this.recordManager = recordManager;
        this.relevant = new ArrayList<>();
    }

    //TODO

    public void search(String patient) {
        for (Record r : recordManager.getRecords()) {
            if (r.getPatient().equalsIgnoreCase(patient)) {
                relevant.add(r);
            }
        }
    }

    public void search(String patient, String clinic) {
        for (Record r : recordManager.getRecords()) {
            if ((r.getPatient().equalsIgnoreCase(patient))
                    && (r.getClinic().equalsIgnoreCase(clinic))) {
                relevant.add(r);
            }
        }
    }

}
