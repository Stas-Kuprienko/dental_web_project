package edu.dental.domain.reports;

import edu.dental.domain.entities.User;

import java.sql.SQLException;

public class ReportService {

    private final ITableTool<String[][]> tableTool;
    //TODO ...
    public ReportService(ITableTool<String[][]> tableTool) {
        this.tableTool = tableTool;
    }
    
    
    public void saveXLSFromDatabase(String tableName) throws SQLException {
        //TODO
    }

    public boolean saveXLSFromReport(User user, MonthlyReport report) {
        String[][] reportData = tableTool.buildTable();
        String tableName = report.getYear() + "_" + report.getMonth() + "_" + user.getId();
        IFileTool fileTool = new XLSFilesTool(tableName, reportData);
        return fileTool.createFile().writeFile();
    }
}
