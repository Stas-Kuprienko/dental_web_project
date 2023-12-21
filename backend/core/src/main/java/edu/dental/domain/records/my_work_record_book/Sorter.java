package edu.dental.domain.records.my_work_record_book;

import edu.dental.domain.records.SorterTool;
import edu.dental.entities.DentalWork;
import utils.collections.SimpleList;

import java.time.LocalDate;
import java.util.List;

public class Sorter implements SorterTool<DentalWork> {

    private final List<DentalWork> data;

    public Sorter(List<DentalWork> data) {
        this.data = data;
    }

    @Override
    public List<DentalWork> doIt(int month) {
        //TODO
        SimpleList<DentalWork> result = new SimpleList<>();
        LocalDate today = LocalDate.now();
        for (DentalWork dw : data) {
            if (dw.getStatus().ordinal() > 1) {
                data.remove(dw);
            } else if (dw.getComplete().isBefore(today)) {
                dw.setStatus(DentalWork.Status.CLOSED);
                result.add(dw);

            }
        }
        return result;
    }
}
