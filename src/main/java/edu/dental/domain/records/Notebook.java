package edu.dental.domain.records;

import edu.dental.domain.entities.WorkRecord;

import java.time.LocalDate;

public abstract class Notebook<T extends WorkRecord> {

    public abstract T createRecord(String patient, String clinic, String product, int quantity, LocalDate complete);

    public abstract T createRecord(String patient, String clinic, LocalDate complete);

    public abstract T addProductToRecord(T workRecord, String product, int quantity);

    public abstract boolean deleteRecord(T workRecord);

    public abstract T searchRecord(String patient, String clinic);

    public abstract T getByID(int id);

}
