package edu.dental.domain.reports.my_report_service;

import edu.dental.database.DatabaseException;
import edu.dental.database.DatabaseService;
import edu.dental.domain.APIManager;
import edu.dental.domain.entities.I_DentalWork;
import edu.dental.domain.entities.SalaryRecord;
import edu.dental.domain.entities.User;
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
            DataArrayTool dataArrayTool = new DataArrayTool(keysArray, (SimpleList<I_DentalWork>) report.getDentalWorks());
            String[][] reportData = dataArrayTool.getResult();
            String tableName = report.getMonth() + "_" + report.getYear();
            IFileTool fileTool = new XLSXFilesTool(tableName, reportData);
            return fileTool.createFile().writeFile();
    }

    @Override
    public MonthlyReport getReportFromDB(User user, String month, String year) throws ReportServiceException {
        try {
            DatabaseService db = APIManager.INSTANCE.getDatabaseService();
            List<I_DentalWork> records = db.getDentalWorkDAO(user).getAllMonthly(month, year);
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