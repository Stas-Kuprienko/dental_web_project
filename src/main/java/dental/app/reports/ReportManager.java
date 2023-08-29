package dental.app.reports;

import dental.app.MyList;
import dental.app.userset.Account;
import dental.app.works.Product;
import dental.app.works.WorkRecord;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;

/**
 * The ReportManager class is used to manage {@link TableReport report} objects.
 *  All methods of the class are static, so the class does not need to implement instances.
 */
public final class ReportManager {

    private ReportManager() {
    }

    public static final String PATH_FOR_REPORTS = "";

    public static void makeReport(Account account) {

    }

    /**
     * Create the file(.xls) with monthly report table.
     * @param account The {@link Account} object that requires.
     * @param report The {@link TableReport} object which needs to convert to a file.
     */
    public static XSSFWorkbook createFileReport(Account account, TableReport report) {

        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet(report.getMonth() + "_" + report.getYear());
        XSSFRow row;

        //create head for the table
        String[] columns = buildTableColumns(account);

        String[][] reportData = buildTableData(columns, report.getRecords());

        for (int i = 0; i < reportData.length; i++) {
            row = sheet.createRow(i);
            //iterate each row and set it to the table
            for (int j = 0; j < reportData[i].length; i++) {
                Cell cell = row.createCell(i);
                cell.setCellValue(reportData[i][j]);
            }
        }
        return workbook;
    }

    public static void writeReportFile(XSSFWorkbook workbook) {
        try(FileOutputStream fileOutput = new FileOutputStream(PATH_FOR_REPORTS)) {
            workbook.write(fileOutput);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * To search a {@link TableReport report} object for a given month.
     * @param account The {@link Account} object that requires.
     * @param month The month for which the report is required.
     * @return The {@link TableReport} object.
     */
    public static TableReport searchReport(Account account, String month) {

        return null;
    }

    private static String[] buildTableColumns(Account account) {
        HashMap<String, Integer> productTypes = account.recordManager.getProductMap();
        String[] columns = new String[productTypes.size() + 2];
        columns[0] = "patient";
        columns[1] = "clinic";
        int i = 2;
        for (String s : productTypes.keySet()) {
            columns[i] = s;
            i++;
        }
        return columns;
    }

    private static String[][] buildTableData(String[] columns, MyList<WorkRecord> workRecords) {
        String[][] result = new String[workRecords.size() + 1][columns.length];

        //put head of the table
        result[0] = columns;

        //counting rows of the table
        int r = 1;
        for (WorkRecord wr : workRecords) {
            String[] row = new String[columns.length];
            row[0] = wr.getPatient();
            row[1] = wr.getClinic();

            //looping product list if it exists
            if (!(wr.getProducts().isEmpty())) {
                for (Product p : wr.getProducts()) {

                    //search index of the product column
                    for (int i = 2; i < columns.length; i++) {
                        if (p.title().equals(columns[i])) {

                            //write quantity of the product items
                            row[i] = String.valueOf(p.quantity());
                            break;
                        }
                    }
                }
            }
            result[r] = row;
            r++;
        }
        return result;
    }

    private static MyList<WorkRecord> selectClosedWork(MyList<WorkRecord> workRecords) {
        MyList<WorkRecord> result = new MyList<>();
        for (WorkRecord wr : workRecords) {
            if (wr.getClosed()) {
                result.add(wr);
            }
        } return result;
    }

}
