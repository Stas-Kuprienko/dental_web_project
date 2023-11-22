package edu.dental.domain.reports.my_report_service;

import edu.dental.database.DBService;
import edu.dental.database.DatabaseException;
import edu.dental.domain.APIManager;
import edu.dental.domain.entities.I_DentalWork;
import edu.dental.domain.entities.SalaryRecord;
import edu.dental.domain.entities.User;
import edu.dental.domain.records.ProductMap;
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
    public boolean saveReportToFile(ProductMap map, MonthlyReport report) {
            DataArrayTool dataArrayTool = new DataArrayTool(map, report.getDentalWorks());
            String[][] reportData = dataArrayTool.getResult();
            String tableName = user.getName() + "_" + report.getMonth() + "_" + report.getYear();
            IFileTool fileTool = new XLSXFilesTool(tableName, reportData);
            return fileTool.createFile().writeFile();
    }

    @Override
    public MonthlyReport getReportFromDB(String month, String year) throws ReportServiceException {
        try {
            DBService db = APIManager.instance().getDBService();
            Collection <I_DentalWork> records = db.getDentalWorkDAO(user).getAllMonthly(month, year);
            return new MonthlyReport(year, month, records);
        } catch (DatabaseException | ClassCastException e) {
            //TODO loggers
            throw new ReportServiceException(e.getMessage(), e.getCause());
        }
    }

    @Override
    public boolean saveSalariesToFile() throws ReportServiceException {
        DBService db = APIManager.instance().getDBService();
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