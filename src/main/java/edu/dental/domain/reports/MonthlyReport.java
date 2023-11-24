package edu.dental.domain.reports;

import edu.dental.domain.entities.I_DentalWork;
import edu.dental.domain.records.WorkRecordBook;
import edu.dental.domain.DatesTool;

import java.util.List;
import java.util.Objects;

public final class MonthlyReport {

    private final String year;
    private final String month;
    private final List<I_DentalWork> dentalWorks;

    public MonthlyReport(String year, String month, List<I_DentalWork> dentalWorks) {
        this.year = year;
        this.month = month;
        this.dentalWorks = dentalWorks;
    }

    public MonthlyReport(List<I_DentalWork> dentalWorks) {
        String[] yearAndMonth = DatesTool.getYearAndMonth(WorkRecordBook.PAY_DAY);
        this.year = yearAndMonth[0];
        this.month = yearAndMonth[1];
        this.dentalWorks = dentalWorks;
    }

    public String getYear() {
        return year;
    }

    public String getMonth() {
        return month;
    }

    public List<I_DentalWork> getDentalWorks() {
        return dentalWorks;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MonthlyReport that = (MonthlyReport) o;
        return Objects.equals(year, that.year) && Objects.equals(month, that.month) &&
                Objects.equals(dentalWorks, that.dentalWorks);
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
                ", dentalWorks=" + dentalWorks +
                '}';
    }
}
