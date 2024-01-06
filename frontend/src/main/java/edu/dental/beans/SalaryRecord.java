package edu.dental.beans;

import java.time.Month;
import java.util.Objects;

public record SalaryRecord(int year, String month, int amount) {

    public int getMonthValue() {
        //TODO - temporary, need to fix!
        return Month.valueOf(month.toUpperCase()).getValue();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SalaryRecord that = (SalaryRecord) o;
        return year == that.year && Objects.equals(month, that.month);
    }

    @Override
    public int hashCode() {
        return Objects.hash(year, month);
    }

    @Override
    public String toString() {
        return "SalaryRecord{" +
                "year=" + year +
                ", month='" + month + '\'' +
                ", amount=" + amount +
                '}';
    }
}
