package edu.dental.domain.records;

import java.util.List;

public interface SorterTool <T> {

    List<T> doIt(int month) throws WorkRecordBookException;
}