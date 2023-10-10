package edu.dental.domain.reports;

import edu.dental.database.DatabaseException;
import edu.dental.domain.entities.WorkRecord;
import edu.dental.domain.records.Mapper;

import java.util.Collection;

public interface ReportService {

    boolean putReportToDB(Collection<WorkRecord> records) throws ReportServiceException;

    boolean putReportToDB(MonthlyReport report) throws ReportServiceException;

    boolean saveTableToFile(Mapper mapper, MonthlyReport report) throws ReportServiceException;

    MonthlyReport getReportFromDB(String month, String year) throws DatabaseException, ReportServiceException;

}
