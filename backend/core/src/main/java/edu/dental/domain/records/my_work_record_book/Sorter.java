package edu.dental.domain.records.my_work_record_book;

import edu.dental.database.DatabaseException;
import edu.dental.database.DatabaseService;
import edu.dental.database.dao.DentalWorkDAO;
import edu.dental.domain.records.SorterTool;
import edu.dental.domain.records.WorkRecordBookException;
import edu.dental.entities.DentalWork;
import utils.collections.SimpleList;

import java.time.LocalDate;
import java.util.List;

public class Sorter implements SorterTool<DentalWork> {

    private final List<DentalWork> works;
    private final int userId;

    public Sorter(List<DentalWork> works, int userId) {
        this.works = works;
        this.userId = userId;
    }

    @Override
    public List<DentalWork> doIt(int month) throws WorkRecordBookException {
        try {
            if (LocalDate.now().getMonth().getValue() == month) {
                return sortCurrentMonth(works);
            } else {
                return sortAnyPreviousMonth(works, month);
            }
        } catch (DatabaseException e) {
            throw new WorkRecordBookException(e.getMessage(), e);
        }
    }

    private SimpleList<DentalWork> sortCurrentMonth(List<DentalWork> list) throws DatabaseException {
        SimpleList<DentalWork> result = new SimpleList<>();
        SimpleList<DentalWork> closed = new SimpleList<>();
        LocalDate now = LocalDate.now();
        for (DentalWork dw : list) {
            if (dw.getComplete().isBefore(now)) {
                if (dw.getStatus().ordinal() < 1) {
                    dw.setStatus(DentalWork.Status.CLOSED);
                    closed.add(dw);
                }
                if (dw.getComplete().getMonthValue() < now.getMonthValue()) {
                    result.add(dw);
                    list.remove(dw);
                }
            }
        }
        if (!closed.isEmpty()) {
            try {
                setStatusToDatabase(closed);
            } catch (DatabaseException e) {
                revertStatus(closed);
                throw e;
            }
        }
        return result;
    }

    private SimpleList<DentalWork> sortAnyPreviousMonth(List<DentalWork> list, int month) throws WorkRecordBookException, DatabaseException {
        if(month == LocalDate.now().getDayOfMonth()) {
            throw new WorkRecordBookException("the given month is the current one, any previous one is required");
        }
        SimpleList<DentalWork> result = new SimpleList<>();
        SimpleList<DentalWork> closed = new SimpleList<>();
        for (DentalWork dw : list) {
            if (dw.getComplete().getMonthValue() <= month) {
                if (dw.getStatus().ordinal() < 1) {
                    dw.setStatus(DentalWork.Status.CLOSED);
                }
                result.add(dw);
                list.remove(dw);
            }
        }
        if (!closed.isEmpty()) {
            try {
                setStatusToDatabase(closed);
            } catch (DatabaseException e) {
                revertStatus(closed);
                throw e;
            }
        }
        return result;
    }

    private void setStatusToDatabase(SimpleList<DentalWork> list) throws DatabaseException {
        DentalWorkDAO dao = DatabaseService.getInstance().getDentalWorkDAO();
        boolean result = dao.setFieldValue(userId, list, "status", DentalWork.Status.CLOSED);
        if (!result) {
            revertStatus(list);
        }
    }

    private void revertStatus(SimpleList<DentalWork> list) {
        list.forEach(e -> e.setStatus(DentalWork.Status.MAKE));
    }
}
