package edu.dental.dto;

import edu.dental.entities.SalaryRecord;

import java.util.Objects;

public record SalaryRecordDto(int year, String month, int amount) {

    public static SalaryRecordDto parse(edu.dental.entities.SalaryRecord record) {
        return new SalaryRecordDto(record.year(), record.month(), record.amount());
    }

    public static SalaryRecordDto[] parseArray(SalaryRecord[] records) {
        SalaryRecordDto[] result = new SalaryRecordDto[records.length];
        int i = 0;
        SalaryRecord record;
        while (i < records.length) {
            record = records[i];
            result[i++] = SalaryRecordDto.parse(record);
        }
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SalaryRecordDto that = (SalaryRecordDto) o;
        return year == that.year && Objects.equals(month, that.month);
    }

    @Override
    public int hashCode() {
        return Objects.hash(year, month);
    }

    @Override
    public String toString() {
        return "SalaryRecordDto{" +
                "year=" + year +
                ", month='" + month + '\'' +
                ", amount=" + amount +
                '}';
    }
}
