package edu.dental.domain.reports.my_report_service;

import edu.dental.domain.entities.User;
import edu.dental.domain.records.ProductMapper;
import edu.dental.domain.reports.IFileTool;
import edu.dental.domain.reports.MonthlyReport;
import edu.dental.domain.reports.ReportService;

public class MyReportService implements ReportService {

    private final User user;

    private MyReportService(User user) {
        this.user = user;
    }
    
    
    public boolean saveTableToFile(String sqlTable) {
        //TODO
        return false;
    }

    public boolean saveTableToFile(ProductMapper productMap, MonthlyReport report) {
        DataArrayTool dataArrayTool = new DataArrayTool(productMap, report.workRecordList());
        String[][] reportData = dataArrayTool.buildTable();
        String tableName = report.getYear() + "_" + report.getMonth() + "_" + user.getId();
        IFileTool fileTool = new XLSXFilesTool(tableName, reportData);
        return fileTool.createFile().writeFile();
    }

    @Override
    public MonthlyReport getReportFromDB(String month, String year) {
        return null;
    }
}
