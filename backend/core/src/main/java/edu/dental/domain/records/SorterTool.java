package edu.dental.domain.records;

import java.util.List;

public interface SorterTool <T> {

    void push(List<T> list);

    List<T> doIt() throws WorkRecordBookException;
}