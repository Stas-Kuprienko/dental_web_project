package edu.dental.domain.reports;

import edu.dental.domain.records.ProductMap;

public interface ReportService {

    boolean saveReportToFile(ProductMap map, MonthlyReport report);

    MonthlyReport getReportFromDB(String month, String year) throws ReportServiceException;

    boolean saveSalariesToFile() throws ReportServiceException;
}
