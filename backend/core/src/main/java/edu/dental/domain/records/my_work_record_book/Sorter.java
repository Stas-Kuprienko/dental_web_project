package edu.dental.domain.records.my_work_record_book;

import edu.dental.domain.records.SorterTool;
import edu.dental.entities.DentalWork;
import utils.collections.SimpleList;

import java.time.LocalDate;
import java.util.List;

public class Sorter implements SorterTool<DentalWork> {

    private final List<DentalWork> works;

    public Sorter(List<DentalWork> works) {
        this.works = works;
    }

    @Override
    public List<DentalWork> doIt(int month) {
        //TODO
        SimpleList<DentalWork> result = new SimpleList<>();
        LocalDate today = LocalDate.now();
        boolean isCurrentMonth = today.getMonth().getValue() == month;
        for (DentalWork dw : works) {
            if (dw.getStatus().ordinal() > 1) {
                works.remove(dw);
                result.add(dw);
            } else {
                if (isCurrentMonth) {
                    if (dw.getComplete().isBefore(today)) {
                        dw.setStatus(DentalWork.Status.CLOSED);
                        result.add(dw);
                    }
                } else {
                    if (dw.getComplete().getMonth().getValue() <= month) {
                        dw.setStatus(DentalWork.Status.CLOSED);
                        result.add(dw);
                    }
                }
            }
        }
        return result;
    }
}
