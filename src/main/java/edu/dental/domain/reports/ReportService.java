package edu.dental.domain.reports;

import java.util.Map;

public interface ReportService {

    boolean saveReportToFile(Map<String, Integer> map, MonthlyReport report) throws ReportServiceException;

    MonthlyReport getReportFromDB(String month, String year) throws ReportServiceException;

}
