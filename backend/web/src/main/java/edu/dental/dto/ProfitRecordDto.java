package edu.dental.dto;

import edu.dental.entities.ProfitRecord;

import java.util.Objects;

public class ProfitRecordDto {

    private int year;
    private String month;
    private int amount;

    public ProfitRecordDto() {}

    public ProfitRecordDto(int year, String month, int amount) {
        this.year = year;
        this.month = month;
        this.amount = amount;
    }

    public ProfitRecordDto(ProfitRecord record) {
        this.year = record.year();
        this.month = record.month();
        this.amount = record.amount();
    }

    public static ProfitRecordDto[] parseArray(ProfitRecord[] records) {
        ProfitRecordDto[] result = new ProfitRecordDto[records.length];
        int i = 0;
        ProfitRecord record;
        while (i < records.length) {
            record = records[i];
            result[i++] = new ProfitRecordDto(record);
        }
        return result;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProfitRecordDto that = (ProfitRecordDto) o;
        return year == that.year && Objects.equals(month, that.month);
    }

    @Override
    public int hashCode() {
        return Objects.hash(year, month);
    }

    @Override
    public String toString() {
        return "ProfitRecordDto{" +
                "year=" + year +
                ", month='" + month + '\'' +
                ", amount=" + amount +
                '}';
    }
}
