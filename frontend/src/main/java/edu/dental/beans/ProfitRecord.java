package edu.dental.beans;

import java.time.Month;
import java.util.Objects;

public class ProfitRecord {

    private int year;
    private String month;
    private int monthValue;
    private int amount;

    public ProfitRecord(int year, String month, int amount) {
        this.year = year;
        this.month = month;
        this.amount = amount;
        this.monthValue = Month.valueOf(month.toUpperCase()).getValue();
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getMonthValue() {
        if (monthValue == 0) {
            this.monthValue = Month.valueOf(month.toUpperCase()).getValue();
        }
        return monthValue;
    }

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
