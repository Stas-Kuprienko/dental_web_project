package edu.dental.domain.reports.my_report_service;

import edu.dental.domain.reports.ReportService;
import edu.dental.domain.reports.ReportException;
import edu.dental.domain.reports.SheetFileTool;
import edu.dental.entities.DentalWork;
import edu.dental.entities.ProfitRecord;

import java.io.OutputStream;
import java.util.List;

public class MyReportService implements ReportService {

    private MyReportService() {}


    @Override
    public boolean writeReportToFile(String[] mapKeys, List<DentalWork> works) throws ReportException {
        DataArrayTool dataArrayTool = new DataArrayTool(mapKeys, works);
        String[][] reportData = dataArrayTool.getResult();
        SheetFileTool fileTool = new XLSXFileTool();
        fileTool.addSheet(reportData);
        return fileTool.writeFile();
    }

    @Override
    public void writeReportToOutput(OutputStream output, String[] mapKeys, List<DentalWork> works) throws ReportException {
        DataArrayTool dataArrayTool = new DataArrayTool(mapKeys, works);
        String[][] reportData = dataArrayTool.getResult();
        SheetFileTool fileTool = new XLSXFileTool();
        fileTool.addSheet(reportData);
        fileTool.writeFile(output);
    }

    @Override
    public boolean writeProfitToFile(ProfitRecord[] profitRecords) throws ReportException {
        DataArrayTool dataTool = new DataArrayTool(profitRecords);
        SheetFileTool fileTool = new XLSXFileTool();
        fileTool.addSheet(dataTool.getResult());
        return fileTool.writeFile();
    }

    @Override
    public void writeProfitToOutput(ProfitRecord[] profitRecords, OutputStream output) throws ReportException {
        DataArrayTool dataTool = new DataArrayTool(profitRecords);
        SheetFileTool fileTool = new XLSXFileTool();
        fileTool.addSheet(dataTool.getResult());
        fileTool.writeFile(output);
    }

    @Override
    public String getFileFormat() {
        return XLSXFileTool.fileFormat;
    }
}