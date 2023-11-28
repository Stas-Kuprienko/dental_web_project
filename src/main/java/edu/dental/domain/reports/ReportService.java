package edu.dental.domain.reports;

import edu.dental.domain.entities.User;

public interface ReportService {

    boolean saveReportToFile(String[] keysArray, MonthlyReport report);

    MonthlyReport getReportFromDB(User user, String month, String year) throws ReportServiceException;

    boolean saveSalariesToFile(User user) throws ReportServiceException;
}
