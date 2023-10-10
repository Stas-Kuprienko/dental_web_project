package edu.dental.domain.reports.my_report_service;

import edu.dental.database.DBServiceManager;
import edu.dental.database.DatabaseException;
import edu.dental.database.DBService;
import edu.dental.database.dao.WorkRecordDAO;
import edu.dental.domain.entities.User;
import edu.dental.domain.entities.WorkRecord;
import edu.dental.domain.records.Mapper;
import edu.dental.domain.reports.IFileTool;
import edu.dental.domain.reports.MonthlyReport;
import edu.dental.domain.reports.ReportService;
import edu.dental.domain.reports.ReportServiceException;

import java.util.Collection;

public class MyReportService implements ReportService {

    private final User user;

    private MyReportService(User user) {
        this.user = user;
    }


    @Override
    public boolean putReportToDB(Collection<WorkRecord> records) throws ReportServiceException {
        MonthlyReport report = new MonthlyReport(records);
        return putReportToDB(report);
    }

    @Override
    public boolean putReportToDB(MonthlyReport report) throws ReportServiceException {
        try {
            DBService db = DBServiceManager.getDBService();
            String yearMonth = report.getYear() + report.getMonth();
            WorkRecordDAO workRecordMySql = db.getWorkRecordDAO(user, yearMonth);
            workRecordMySql.putAll(report.getWorkRecordList());
        } catch (DatabaseException e) {
            throw new ReportServiceException(e.getMessage(), e.getCause());
        }
        return false;
    }

    @Override
    public boolean saveTableToFile(Mapper mapper, MonthlyReport report) throws ReportServiceException {
        try {
            DataArrayTool dataArrayTool = new DataArrayTool(mapper, report.getWorkRecordList());
            String[][] reportData = dataArrayTool.buildTable();
            String tableName = report.getYear() + "_" + report.getMonth() + "_" + user.getName();
            IFileTool fileTool = new XLSXFilesTool(tableName, reportData);
            return fileTool.createFile().writeFile();
        } catch (ClassCastException e) {
            throw new ReportServiceException(e.getMessage(), e.getCause());
        }
    }

    @Override
    public MonthlyReport getReportFromDB(String month, String year) throws ReportServiceException {
        try {
            DBService db = DBServiceManager.getDBService();
            Collection <WorkRecord> records = db.getWorkRecordDAO(user, year + month).getAll();
            return new MonthlyReport(year, month, records);
        } catch (DatabaseException | ClassCastException e) {
            throw new ReportServiceException(e.getMessage(), e.getCause());
        }
    }
}