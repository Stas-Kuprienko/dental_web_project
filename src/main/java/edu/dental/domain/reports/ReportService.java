package edu.dental.domain.reports;

import edu.dental.domain.entities.IDentalWork;
import edu.dental.domain.entities.User;

import java.util.List;

public interface ReportService {

    boolean saveReportToFile(String[] keysArray, MonthlyReport report);

    boolean saveReportToFile(String[] keysArray, List<IDentalWork> works);

    MonthlyReport getReportFromDB(User user, String month, String year) throws ReportServiceException;

    boolean saveSalariesToFile(User user) throws ReportServiceException;
}
