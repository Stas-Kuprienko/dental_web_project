package edu.dental.domain.records.my_work_record_book;

import edu.dental.database.DatabaseException;
import edu.dental.database.DatabaseService;
import edu.dental.database.dao.DentalWorkDAO;
import edu.dental.domain.records.SorterTool;
import edu.dental.domain.records.WorkRecordException;
import edu.dental.entities.DentalWork;
import utils.collections.SimpleList;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;
import java.util.ListIterator;

public class Sorter implements SorterTool<DentalWork> {

    private List<DentalWork> works;
    private final DentalWorkDAO dao;
    private final int userId;
    private final int month;
    private final int year;


    public Sorter(int userId, int month, int year) {
        this.dao = DatabaseService.getInstance().getDentalWorkDAO();
        this.userId = userId;
        this.month = month;
        this.year = year;
    }


    @Override
    public void push(List<DentalWork> works) {
        this.works = works;
    }

    @Override
    public List<DentalWork> doIt() throws DatabaseException {
        LocalDate now = LocalDate.now();
            if (now.getMonth().getValue() == month && now.getYear() == year) {
                return sortCurrentMonth(works);
            } else {
                return sortAnyPreviousMonth(works, year, month);
            }
    }

    private SimpleList<DentalWork> sortCurrentMonth(List<DentalWork> list) throws DatabaseException {
        SimpleList<DentalWork> result = new SimpleList<>();
        SimpleList<DentalWork> closed = new SimpleList<>();
        LocalDate now = LocalDate.now();
        ListIterator<DentalWork> iterator = list.listIterator();
        DentalWork dw;
        while (iterator.hasNext()) {
            dw = iterator.next();
            if (dw.getComplete().isBefore(now)) {
                if (dw.getStatus().ordinal() < 1) {
                    dw.setStatus(DentalWork.Status.CLOSED);
                    closed.add(dw);
                }
                if (dw.getComplete().getMonthValue() != now.getMonthValue()) {
                    result.add(dw);
                    iterator.remove();
                }
            }
        }
        if (!closed.isEmpty()) {
            try {
                setStatusToDatabase(closed);
            } catch (DatabaseException e) {
                revertStatus(closed); throw e;
            }
        }
        if (!result.isEmpty()) {
            try {
                LocalDate previousMonth = LocalDate.now().minusMonths(1);
                setReportIdToDatabase(result, previousMonth.getYear(), previousMonth.getMonthValue());
            } catch (DatabaseException e) {
                revertReport(result); throw e;
            }
        }
        return result;
    }

    private SimpleList<DentalWork> sortAnyPreviousMonth(List<DentalWork> list, int year, int month) throws DatabaseException {
        LocalDate now = LocalDate.now();
        if (month == now.getMonthValue() && year == now.getYear()) {
            return sortCurrentMonth(list);
        }
        SimpleList<DentalWork> result = new SimpleList<>();
        SimpleList<DentalWork> closed = new SimpleList<>();
        ListIterator<DentalWork> iterator = list.listIterator();
        DentalWork dw;
        while (iterator.hasNext()) {
            dw = iterator.next();

            if (dw.getComplete().getYear() < year ||
                    dw.getComplete().getYear() == year && dw.getComplete().getMonthValue() <= month) {

                if (dw.getStatus().ordinal() < 1) {
                    dw.setStatus(DentalWork.Status.CLOSED);
                    closed.add(dw);
                }
                result.add(dw);
                iterator.remove();
            }
        }
        if (!closed.isEmpty()) {
            setStatusToDatabase(closed);
        }
        if (!result.isEmpty()) {
            setReportIdToDatabase(result, year, month);
        }
        return result;
    }


    private void setStatusToDatabase(SimpleList<DentalWork> list) throws DatabaseException {
        boolean result;
        try {
            result = dao.setFieldValue(userId, list, "status", DentalWork.Status.CLOSED);
        } catch (DatabaseException e) {
            revertStatus(list); throw e;
        }
        if (!result) {
            revertStatus(list);
        }
    }

    private void revertStatus(SimpleList<DentalWork> list) {
        list.forEach(e -> e.setStatus(DentalWork.Status.MAKE));

    }

    private void setReportIdToDatabase(SimpleList<DentalWork> list, int year, int month) throws DatabaseException {
        int result;
        try {
            result = dao.setReportId(userId, list, Month.of(month).toString(), Integer.toString(year));
        } catch (DatabaseException e) {
            revertReport(list); throw e;
        }
        if (result <= 0) {
            revertReport(list);
        }
    }

    private void revertReport(SimpleList<DentalWork> list) {
        list.forEach(e -> e.setReportId(0));
    }
}
