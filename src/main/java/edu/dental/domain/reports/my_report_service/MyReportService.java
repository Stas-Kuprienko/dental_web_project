package edu.dental.domain.reports.my_report_service;

import edu.dental.database.DatabaseException;
import edu.dental.database.DatabaseService;
import edu.dental.domain.APIManager;
import edu.dental.domain.DatesTool;
import edu.dental.domain.entities.IDentalWork;
import edu.dental.domain.entities.SalaryRecord;
import edu.dental.domain.entities.User;
import edu.dental.domain.records.WorkRecordBook;
import edu.dental.domain.reports.IFileTool;
import edu.dental.domain.reports.MonthlyReport;
import edu.dental.domain.reports.ReportService;
import edu.dental.domain.reports.ReportServiceException;
import utils.collections.SimpleList;

import java.util.List;

public class MyReportService implements ReportService {

    private MyReportService() {}


    @Override
    public boolean saveReportToFile(String[] keysArray, MonthlyReport report) {
        DataArrayTool dataArrayTool = new DataArrayTool(keysArray, (SimpleList<IDentalWork>) report.dentalWorks());
        String[][] reportData = dataArrayTool.getResult();
        String tableName = report.month() + "_" + report.year();
        IFileTool fileTool = new XLSXFilesTool(tableName, reportData);
        return fileTool.createFile().writeFile();
    }

    @Override
    public boolean saveReportToFile(String[] keysArray, List<IDentalWork> works) {
        String[] yearAndMonth = DatesTool.getYearAndMonth(WorkRecordBook.PAY_DAY);
        DataArrayTool dataArrayTool = new DataArrayTool(keysArray, (SimpleList<IDentalWork>) works);
        String[][] reportData = dataArrayTool.getResult();
        String tableName = yearAndMonth[1] + "_" + yearAndMonth[0];
        IFileTool fileTool = new XLSXFilesTool(tableName, reportData);
        return fileTool.createFile().writeFile();
    }

    @Override
    public MonthlyReport getReportFromDB(User user, String month, String year) throws ReportServiceException {
        try {
            DatabaseService db = APIManager.INSTANCE.getDatabaseService();
            List<IDentalWork> records = db.getDentalWorkDAO(user).getAllMonthly(month, year);
            return new MonthlyReport(year, month, records);
        } catch (DatabaseException | ClassCastException e) {
            //TODO loggers
            throw new ReportServiceException(e.getMessage(), e.getCause());
        }
    }

    @Override
    public boolean saveSalariesToFile(User user) throws ReportServiceException {
        DatabaseService db = APIManager.INSTANCE.getDatabaseService();
        try {
            SalaryRecord[] salaries = db.countAllSalaries(user);
            DataArrayTool arrayTool = new DataArrayTool(salaries);
            String nameTable = user.getName() + "_salaries_list";
            IFileTool fileTool = new XLSXFilesTool(nameTable, arrayTool.getResult());
            return fileTool.createFile().writeFile();
        } catch (DatabaseException e) {
            throw new ReportServiceException(e.getMessage(), e.getCause());
        }
    }
}