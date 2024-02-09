package edu.dental.domain.records;

import edu.dental.database.DatabaseException;

import java.util.List;

public interface SorterTool <T> {

    void push(List<T> list);

    List<T> doIt() throws DatabaseException;
}