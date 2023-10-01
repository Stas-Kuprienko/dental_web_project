package edu.dental.domain.records;

import edu.dental.domain.entities.WorkRecord;

public interface WorkRecordBook {

    WorkRecord createRecord(String patient, String clinic, String product, int quantity, String complete) throws WorkRecordBookException;

    WorkRecord createRecord(String patient, String clinic, String complete) throws WorkRecordBookException;

    WorkRecord addProductToRecord(WorkRecord workRecord, String product, int quantity) throws WorkRecordBookException;

    boolean deleteRecord(WorkRecord workRecord);

    WorkRecord searchRecord(String patient, String clinic) throws WorkRecordBookException;

    WorkRecord getByID(int id) throws WorkRecordBookException;

}
