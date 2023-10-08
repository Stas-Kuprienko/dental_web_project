package edu.dental.domain.reports.my_report_service;

import edu.dental.database.DBServiceManager;
import edu.dental.database.DatabaseException;
import edu.dental.database.interfaces.DBService;
import edu.dental.database.mysql_api.dao.WorkRecordMySql;
import edu.dental.domain.entities.User;
import edu.dental.domain.entities.WorkRecord;
import edu.dental.domain.records.ProductMapper;
import edu.dental.domain.reports.IFileTool;
import edu.dental.domain.reports.MonthlyReport;
import edu.dental.domain.reports.ReportService;
import edu.dental.domain.reports.ReportServiceException;
import edu.dental.utils.data_structures.MyList;

public class MyReportService implements ReportService {

    private final User user;

    private MyReportService(User user) {
        this.user = user;
    }


    @Override
    public boolean putReportToDB(MonthlyReport report) throws ReportServiceException {
        try {
            DBService db = DBServiceManager.getDBService();
            String yearMonth = report.getYear() + report.getMonth();
            WorkRecordMySql workRecordMySql = (WorkRecordMySql) db.getWorkRecordDAO(user, yearMonth);
            workRecordMySql.putAll(report.getWorkRecordList());
        } catch (DatabaseException e) {
            throw new ReportServiceException(e.getMessage(), e.getCause());
        }
        return false;
    }

    @Override
    public boolean saveTableToFile(ProductMapper productMap, MonthlyReport report) throws ReportServiceException {
        try {
            DataArrayTool dataArrayTool = new DataArrayTool(productMap, report.getWorkRecordList());
            String[][] reportData = dataArrayTool.buildTable();
            String tableName = report.getYear() + "_" + report.getMonth() + "_" + user.getId();
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
            @SuppressWarnings("unchecked")
            MyList<WorkRecord> records = (MyList<WorkRecord>) db.getWorkRecordDAO(user, year + month).getAll();
            return new MonthlyReport(year, month, records);
        } catch (DatabaseException | ClassCastException e) {
            throw new ReportServiceException(e.getMessage(), e.getCause());
        }
    }
}