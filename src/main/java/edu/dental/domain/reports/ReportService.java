package edu.dental.domain.reports;

import edu.dental.domain.entities.User;
import edu.dental.domain.entities.WorkRecord;
import edu.dental.domain.records.ProductMapper;
import edu.dental.utils.data_structures.MyList;

import java.sql.SQLException;
import java.time.LocalDate;

public class ReportService {

    private ReportService() {}
    
    
    public static void saveXLSFromDatabase(String tableName) throws SQLException {
        //TODO
    }

    public static void saveXLSFromReport(User user, ProductMapper productMap, MonthlyReport report) {
        String[] columns = DataTablesTool.buildTableColumns(productMap);
        String[][] reportData = DataTablesTool.buildTableData(columns, report.workRecordList());
        String tableName = report.getYear() + "_" + report.getMonth() + "_" + user.getId();
        XLSFilesTool.writeReportFile(XLSFilesTool.createFileReport(tableName, reportData));
    }


    private static MyList<WorkRecord> selectClosedWork(MyList<WorkRecord> workRecords) {
        MyList<WorkRecord> result = new MyList<>();
        for (WorkRecord wr : workRecords) {
            //TODO selecting by date
            if ((wr.isClosed())||(LocalDate.now().isAfter(wr.getComplete()))) {
                result.add(wr);
            }
        } return result;
    }
}
