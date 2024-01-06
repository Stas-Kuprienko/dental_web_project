package edu.dental.domain.reports.my_report_service;

import edu.dental.database.DatabaseException;
import edu.dental.database.DatabaseService;
import edu.dental.database.dao.DentalWorkDAO;
import edu.dental.database.dao.ReportDAO;
import edu.dental.domain.APIManager;
import edu.dental.domain.reports.IFileTool;
import edu.dental.domain.reports.ReportService;
import edu.dental.domain.reports.ReportServiceException;
import edu.dental.entities.DentalWork;
import edu.dental.entities.SalaryRecord;
import utils.collections.SimpleList;

import java.io.OutputStream;
import java.time.Month;
import java.util.List;
import java.util.NoSuchElementException;

public class MyReportService implements ReportService {

    private MyReportService() {}


    @Override
    public OutputStream writeReportToOutput(OutputStream output, String[] keys, List<DentalWork> works) throws ReportServiceException {
        DataArrayTool dataArrayTool = new DataArrayTool(keys, (SimpleList<DentalWork>) works);
        String[][] reportData = dataArrayTool.getResult();
        IFileTool fileTool = new XLSXFilesTool(reportData);
        return fileTool.createFile().writeFile(output);
    }

    @Override
    public List<DentalWork> getReportFromDB(int userId, int monthValue, int year) throws ReportServiceException {
        String month = Month.of(monthValue).toString();
        return getReportFromDB(userId, month, String.valueOf(year));
    }

    @Override
    public List<DentalWork> getReportFromDB(int userId, String month, String year) throws ReportServiceException {
        try {
            DatabaseService db = APIManager.INSTANCE.getDatabaseService();
            return db.getDentalWorkDAO().getAllMonthly(userId, month, year);
        } catch (DatabaseException | ClassCastException e) {
            //TODO loggers
            throw new ReportServiceException(e.getMessage(), e.getCause());
        }
    }

    @Override
    public List<DentalWork> searchRecords(int userId, String[] fields, String[] args) throws ReportServiceException {
        DentalWorkDAO dao = DatabaseService.getInstance().getDentalWorkDAO();
        try {
            return dao.search(userId, fields, args);
        } catch (DatabaseException e) {
            throw new ReportServiceException(e.getMessage(), e);
        }
    }

    @Override
    public DentalWork getByIDFromDatabase(int userId, int workId) throws ReportServiceException {
        DentalWorkDAO dao = DatabaseService.getInstance().getDentalWorkDAO();
        try {
            DentalWork dentalWork = dao.get(userId, workId);
            if (dentalWork == null) {
                throw new ReportServiceException(new NoSuchElementException());
            } else {
                return dentalWork;
            }
        } catch (DatabaseException e) {
            throw new ReportServiceException(e);
        }
    }

    @Override
    public boolean saveSalariesToFile(int userId) throws ReportServiceException {
        ReportDAO report = APIManager.INSTANCE.getDatabaseService().getReportDAO();
        try {
            SalaryRecord[] salaries = report.countAllSalaries(userId);
            DataArrayTool dataTool = new DataArrayTool(salaries);
            String nameTable = "salaries_list";
            IFileTool fileTool = new XLSXFilesTool(nameTable, dataTool.getResult());
            return fileTool.createFile().writeFile();
        } catch (DatabaseException e) {
            throw new ReportServiceException(e.getMessage(), e.getCause());
        }
    }

    @Override
    public String getFileFormat() {
        return XLSXFilesTool.fileFormat;
    }
}