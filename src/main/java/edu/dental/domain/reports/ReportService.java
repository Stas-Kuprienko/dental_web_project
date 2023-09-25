package edu.dental.domain.reports;

import edu.dental.domain.records.ProductMapper;

public interface ReportService {

    boolean saveTableToFile(String sqlTable);

    boolean saveTableToFile(ProductMapper productMap, MonthlyReport report);

    MonthlyReport getReportFromDB(String month, String year);

}
