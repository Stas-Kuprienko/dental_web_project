package edu.dental.dto;

import edu.dental.entities.ProfitRecord;

import java.util.Objects;

public record ProfitRecordDto(int year, String month, int amount) {

    public static ProfitRecordDto parse(ProfitRecord record) {
        return new ProfitRecordDto(record.year(), record.month(), record.amount());
    }

    public static ProfitRecordDto[] parseArray(ProfitRecord[] records) {
        ProfitRecordDto[] result = new ProfitRecordDto[records.length];
        int i = 0;
        ProfitRecord record;
        while (i < records.length) {
            record = records[i];
            result[i++] = ProfitRecordDto.parse(record);
        }
        return result;
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
