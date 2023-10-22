package edu.dental.domain.reports.my_report_service;

import edu.dental.database.DBService;
import edu.dental.database.DBServiceManager;
import edu.dental.database.DatabaseException;
import edu.dental.domain.entities.I_WorkRecord;
import edu.dental.domain.entities.User;
import edu.dental.domain.records.ProductMap;
import edu.dental.domain.reports.IFileTool;
import edu.dental.domain.reports.MonthlyReport;
import edu.dental.domain.reports.ReportService;
import edu.dental.domain.reports.ReportServiceException;

import java.util.Collection;
import java.util.Map;

public class MyReportService implements ReportService {

    private final User user;

    private MyReportService(User user) {
        this.user = user;
    }


    @Override
    public boolean saveReportToFile(Map<String, Integer> map, MonthlyReport report) throws ReportServiceException {
        try {
            DataArrayTool dataArrayTool = new DataArrayTool((ProductMap) map, report.getWorkRecordList());
            String[][] reportData = dataArrayTool.buildTable();
            String tableName = user.getName() + "_" + report.getMonth() + "_" + report.getYear();
            IFileTool fileTool = new XLSXFilesTool(tableName, reportData);
            return fileTool.createFile().writeFile();
        } catch (ClassCastException e) {
            //TODO loggers
            throw new ReportServiceException(e.getMessage(), e.getCause());
        }
    }

    @Override
    public MonthlyReport getReportFromDB(String month, String year) throws ReportServiceException {
        try {
            DBService db = DBServiceManager.getDBService();
            Collection <I_WorkRecord> records = db.getWorkRecordDAO(user).getAllMonthly(month, year);
            return new MonthlyReport(year, month, records);
        } catch (DatabaseException | ClassCastException e) {
            //TODO loggers
            throw new ReportServiceException(e.getMessage(), e.getCause());
        }
    }
}