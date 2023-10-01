package edu.dental.domain.records;

import edu.dental.domain.entities.WorkRecord;

public interface WorkRecordBook {

    WorkRecord createRecord(String patient, String clinic, String product, int quantity, String complete);

    WorkRecord createRecord(String patient, String clinic, String complete);

    WorkRecord addProductToRecord(WorkRecord workRecord, String product, int quantity);

    boolean deleteRecord(WorkRecord workRecord);

    WorkRecord searchRecord(String patient, String clinic);

    WorkRecord getByID(int id);

}
