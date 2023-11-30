package edu.dental.domain.reports;

import edu.dental.domain.APIManager;
import edu.dental.domain.entities.DentalWork;
import edu.dental.domain.entities.User;

import java.util.List;

public interface ReportService {

    static ReportService getInstance() {
        return APIManager.INSTANCE.getReportService();
    }

    boolean saveReportToFile(String[] keysArray, MonthlyReport report);

    boolean saveReportToFile(String[] keysArray, List<DentalWork> works);

    MonthlyReport getReportFromDB(User user, String month, String year) throws ReportServiceException;

    boolean saveSalariesToFile(User user) throws ReportServiceException;
}
