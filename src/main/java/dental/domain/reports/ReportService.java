package dental.domain.reports;

import dental.domain.data_structures.MyList;
import dental.domain.userset.Account;
import dental.domain.works.WorkRecord;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.HashMap;

/**
 * The ReportService class is used to manage reports.
 *  All methods of the class are static, so the class does not need to implement instances.
 */
public final class ReportService {

    private ReportService() {}


    /**
     * To search a {@link TableReport report} object for a given month.
     * @param account The {@link Account} object that requires.
     * @param month The month for which the report is required.
     * @param year The year of the month.
     * @return The {@link TableReport} object.
     */
    public static TableReport searchReport(Account account, String month, String year) {

        return null;
    }

    public static void saveReportToFile(Account account) {
        TableReport report = createTableReport(account);
        ReportFilesTool.writeReportFile(ReportFilesTool.createFileReport(account, report));
    }

    public static void saveReportToFile(Account account, String tableName) throws SQLException {
        ReportFilesTool.writeReportFile(ReportFilesTool.createFileReport(account, tableName));
    }

    public static void saveReportToFile(Account account, TableReport report) {
        ReportFilesTool.writeReportFile(ReportFilesTool.createFileReport(account, report));
    }

    public static TableReport createTableReport(Account account) {
        MyList<WorkRecord> workRecords = selectClosedWork(account.recordManager.workRecords);
        String month = LocalDate.now().getMonth().toString();
        String year = String.valueOf(LocalDate.now().getYear());
        TableReport report = new TableReport(month, year, workRecords);
        if (account.getReports().get(year) != null) {
            account.getReports().get(year).put(month, report);
        } else {
            HashMap<String, TableReport> reportMap = new HashMap<>();
            reportMap.put(year, report);
            account.getReports().put(year, reportMap);
        }
        return report;
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