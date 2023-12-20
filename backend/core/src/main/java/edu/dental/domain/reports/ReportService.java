package edu.dental.domain.reports;

import edu.dental.domain.APIManager;
import edu.dental.entities.DentalWork;
import edu.dental.entities.User;

import java.io.OutputStream;
import java.util.List;

public interface ReportService {

    static ReportService getInstance() {
        return APIManager.INSTANCE.getReportService();
    }

    boolean saveReportToFile(String[] keysArray, MonthlyReport report) throws ReportServiceException;

    boolean saveReportToFile(String[] keysArray, List<DentalWork> works) throws ReportServiceException;

    OutputStream saveReportToFile(OutputStream output, String[] keysArray, List<DentalWork> works) throws ReportServiceException;

    MonthlyReport getReportFromDB(User user, String month, String year) throws ReportServiceException;

    boolean saveSalariesToFile(User user) throws ReportServiceException;

    String getFileFormat();
}
