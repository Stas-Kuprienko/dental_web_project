package edu.dental.domain.reports;

import edu.dental.domain.entities.DentalWork;

import java.util.List;
import java.util.Objects;

public record MonthlyReport(String year, String month, List<DentalWork> dentalWorks) {

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
        return Objects.hash(year, month, dentalWorks);
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
