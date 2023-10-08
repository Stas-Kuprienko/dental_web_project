package edu.dental.domain.records.my_work_record_book;

import edu.dental.domain.entities.WorkRecord;
import edu.dental.domain.records.WorkRecordBookException;
import edu.dental.utils.data_structures.MyList;

import java.time.LocalDate;

public class Distributor {

    private final MyList<WorkRecord> result;

    public Distributor(MyList<WorkRecord> list) throws WorkRecordBookException {
        this.result = new MyList<>();
        if (!execute(list)) {
            throw new WorkRecordBookException("distribution is failed");
        }
    }

    private boolean execute(MyList<WorkRecord> list) {
        for (WorkRecord wr : list) {
            if (isDone(wr)) {
                result.add(wr);
                list.remove(wr);
            }
        }
        return false;
    }

    private boolean isDone(WorkRecord wr) {
        if (wr.isClosed()) {

        } else if (isComplete(wr)) {

        }
    }

    private boolean isComplete(WorkRecord wr) {
        LocalDate today = LocalDate.now();
    }

    public MyList<WorkRecord> getResult() {
        return result;
    }
}
