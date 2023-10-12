package edu.dental.domain.reports;

import edu.dental.database.DatabaseException;
import edu.dental.domain.entities.I_WorkRecord;

import java.util.Collection;
import java.util.Map;

public interface ReportService {

    boolean putReportToDB(Collection<I_WorkRecord> records) throws ReportServiceException;

    boolean putReportToDB(MonthlyReport report) throws ReportServiceException;

    boolean saveTableToFile(Map<String, Integer> map, MonthlyReport report) throws ReportServiceException;

    MonthlyReport getReportFromDB(String month, String year) throws DatabaseException, ReportServiceException;

}
