package edu.dental.domain.reports;

import edu.dental.domain.entities.I_WorkRecord;
import edu.dental.domain.records.WorkRecordBook;
import edu.dental.utils.DatesTool;
import edu.dental.utils.data_structures.MyList;

import java.util.Collection;
import java.util.Objects;

public final class MonthlyReport {

    private final String year;
    private final String month;
    private final Collection<I_WorkRecord> workRecordList;

    public MonthlyReport(String year, String month, Collection<I_WorkRecord> workRecordList) {
        this.year = year;
        this.month = month;
        this.workRecordList = workRecordList;
    }

    public MonthlyReport(Collection<I_WorkRecord> workRecordList) {
        String[] yearAndMonth = DatesTool.getYearAndMonth(WorkRecordBook.PAY_DAY);
        this.year = yearAndMonth[0];
        this.month = yearAndMonth[1];
        this.workRecordList = workRecordList;
    }

    public String getYear() {
        return year;
    }

    public String getMonth() {
        return month;
    }

    public MyList<I_WorkRecord> getWorkRecordList() {
        return (MyList<I_WorkRecord>) workRecordList;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MonthlyReport that = (MonthlyReport) o;
        return Objects.equals(year, that.year) && Objects.equals(month, that.month) &&
                Objects.equals(workRecordList, that.workRecordList);
    }

    @Override
    public int hashCode() {
        return Objects.hash(year, month);
    }

    @Override
    public String toString() {
        return "MonthlyReport{" +
                "year='" + year + '\'' +
                ", month='" + month + '\'' +
                ", workRecordList=" + workRecordList +
                '}';
    }
}
