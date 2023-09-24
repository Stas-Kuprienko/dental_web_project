package edu.dental.domain.reports;

import edu.dental.domain.entities.WorkRecord;
import edu.dental.utils.data_structures.MyList;

import java.util.Objects;

public record MonthlyReport(String year, String month, MyList<WorkRecord> workRecordList) {


    public String getYear() {
        return year;
    }

    public String getMonth() {
        return month;
    }

    public MyList<WorkRecord> getWorkRecordList() {
        return workRecordList;
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
