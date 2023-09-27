package edu.dental.database.mysql_api;

import edu.dental.database.interfaces.TablesCreator;

public class MySqlTablesCreator implements TablesCreator {
    @Override
    public boolean createProductMapTable(int userID) {
        return false;
    }

    @Override
    public boolean createWorkRecordTable(int userID, String month) {
        return false;
    }

    @Override
    public boolean createProductTable(int userID, String month) {
        return false;
    }
}
