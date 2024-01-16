package edu.dental.database.dao;

import edu.dental.database.DatabaseException;
import edu.dental.entities.ProfitRecord;

public interface ProfitRecordDAO {

    ProfitRecord[] countAllProfits(int userId) throws DatabaseException;

    ProfitRecord countProfitForMonth(int userId, int year, String month) throws DatabaseException;
}
