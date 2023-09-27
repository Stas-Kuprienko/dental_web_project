package edu.dental.database.interfaces;

public interface TablesCreator {

    boolean createProductMapTable(int userID);

    boolean createWorkRecordTable(int userID, String month);

    boolean createProductTable(int userID, String month);

}
