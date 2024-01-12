package edu.dental.domain.reports;

import edu.dental.domain.APIManager;
import edu.dental.entities.DentalWork;
import edu.dental.entities.ProfitRecord;

import java.io.OutputStream;
import java.util.List;

public interface ReportService {

    static ReportService getInstance() {
        return APIManager.INSTANCE.getReportService();
    }

    boolean writeReportToFile(String[] mapKeys, List<DentalWork> works) throws ReportServiceException;

    void writeReportToOutput(OutputStream output, String[] mapKeys, List<DentalWork> works) throws ReportServiceException;

    boolean writeProfitToFile(ProfitRecord[] profitRecords) throws ReportServiceException;

    void writeProfitToOutput(ProfitRecord[] profitRecords, OutputStream output) throws ReportServiceException;

    String getFileFormat();
}