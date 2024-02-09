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

    boolean writeReportToFile(String[] mapKeys, List<DentalWork> works) throws ReportException;

    void writeReportToOutput(OutputStream output, String[] mapKeys, List<DentalWork> works) throws ReportException;

    boolean writeProfitToFile(ProfitRecord[] profitRecords) throws ReportException;

    void writeProfitToOutput(ProfitRecord[] profitRecords, OutputStream output) throws ReportException;

    String getFileFormat();
}