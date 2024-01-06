package edu.dental.domain.reports;

import edu.dental.domain.APIManager;
import edu.dental.entities.DentalWork;

import java.io.OutputStream;
import java.util.List;

public interface ReportService {

    static ReportService getInstance() {
        return APIManager.INSTANCE.getReportService();
    }

    OutputStream writeReportToOutput(OutputStream output, String[] keys, List<DentalWork> works) throws ReportServiceException;

    List<DentalWork> getReportFromDB(int userId, int monthValue, int year) throws ReportServiceException;

    List<DentalWork> getReportFromDB(int userId, String month, String year) throws ReportServiceException;

    List<DentalWork> searchRecords(int userId, String[] fields, String[] args) throws ReportServiceException;

    boolean saveSalariesToFile(int userId) throws ReportServiceException;

    String getFileFormat();
}