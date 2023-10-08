package edu.dental.domain.reports;

import edu.dental.database.DatabaseException;
import edu.dental.domain.records.ProductMapper;

public interface ReportService {

    boolean putReportToDB(MonthlyReport report) throws ReportServiceException;

    boolean saveTableToFile(ProductMapper productMap, MonthlyReport report) throws ReportServiceException;

    MonthlyReport getReportFromDB(String month, String year) throws DatabaseException, ReportServiceException;

}
