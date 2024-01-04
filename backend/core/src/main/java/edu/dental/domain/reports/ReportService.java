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

    OutputStream saveReportToFile(OutputStream output, String[] keysArray, List<DentalWork> works) throws ReportServiceException;

    MonthlyReport getReportFromDB(int userId, String month, String year) throws ReportServiceException;

    boolean saveSalariesToFile(int userId) throws ReportServiceException;

    String getFileFormat();
}