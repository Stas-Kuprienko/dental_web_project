package edu.dental.entities;

import java.util.Objects;

public record ProfitRecord(int year, String month, int amount) {

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProfitRecord that = (ProfitRecord) o;
        return year == that.year && Objects.equals(month, that.month);
    }

    @Override
    public int hashCode() {
        return Objects.hash(year, month);
    }

    @Override
    public String toString() {
        return "ProfitRecord{" +
                "year=" + year +
                ", month='" + month + '\'' +
                ", amount=" + amount +
                '}';
    }
}
