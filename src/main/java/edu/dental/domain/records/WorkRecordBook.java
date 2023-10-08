package edu.dental.domain.records;

import edu.dental.domain.entities.WorkRecord;

import java.util.Collection;
import java.util.Map;

public interface WorkRecordBook {

    byte PAY_DAY = 10;

    WorkRecord createRecord(String patient, String clinic, String product, int quantity, String complete) throws WorkRecordBookException;

    WorkRecord createRecord(String patient, String clinic, String complete) throws WorkRecordBookException;

    WorkRecord addProductToRecord(WorkRecord workRecord, String product, int quantity) throws WorkRecordBookException;

    WorkRecord removeProduct(WorkRecord workRecord, String product) throws WorkRecordBookException;

    boolean deleteRecord(WorkRecord workRecord);

    WorkRecord searchRecord(String patient, String clinic) throws WorkRecordBookException;

    WorkRecord getByID(int id) throws WorkRecordBookException;

    Collection<WorkRecord> sorting();

    Collection<WorkRecord> getList();

    Map<String, Integer> getMap();
}
