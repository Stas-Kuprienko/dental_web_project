package edu.dental.domain.reports;

import edu.dental.database.DatabaseException;
import edu.dental.domain.entities.WorkRecord;
import edu.dental.domain.records.ProductMapper;

import java.util.Collection;

public interface ReportService {

    boolean putReportToDB(Collection<WorkRecord> records) throws ReportServiceException;

    boolean putReportToDB(MonthlyReport report) throws ReportServiceException;

    boolean saveTableToFile(ProductMapper productMap, MonthlyReport report) throws ReportServiceException;

    MonthlyReport getReportFromDB(String month, String year) throws DatabaseException, ReportServiceException;

}
