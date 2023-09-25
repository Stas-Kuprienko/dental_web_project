package edu.dental.domain.records;

import edu.dental.domain.entities.WorkRecord;

import java.time.LocalDate;

public interface WorkRecordBook {

    WorkRecord createRecord(String patient, String clinic, String product, int quantity, LocalDate complete);

    WorkRecord createRecord(String patient, String clinic, LocalDate complete);

    WorkRecord addProductToRecord(WorkRecord workRecord, String product, int quantity);

    boolean deleteRecord(WorkRecord workRecord);

    WorkRecord searchRecord(String patient, String clinic);

    WorkRecord getByID(int id);

}
