package dental.domain.reports;

import dental.domain.data_structures.MyList;
import dental.domain.userset.Account;
import dental.domain.works.Product;
import dental.domain.works.WorkRecord;
import dental.database.requests.reports.TableReportDBInstantiation;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;

final class ReportFilesTool {

    private ReportFilesTool() {}

    private static final String fileFormat = ".xlsx";

    private static final String PATH_FOR_REPORTS = "src/main/resources/";

    /**
     * Create the file(.xls) with monthly report table.
     * @param account The {@link Account} object that requires.
     * @param report The {@link TableReport} object which needs to convert to a file.
     */
    static XSSFWorkbook createFileReport(Account account, TableReport report) {
        String tableName = account.getName() + "_" + report.getMonth() + "_" + report.getYear();
        XSSFBox xssfBox = new XSSFBox(tableName);

        //create head for the table
        String[] columns = buildTableColumns(account);
        String[][] reportData = buildTableData(columns, report.getRecords());

        putDataInSheet(xssfBox, reportData);
        return xssfBox.workbook;
    }

    static XSSFWorkbook createFileReport(Account account, String tableName) throws SQLException {
        //TODO verify table name
        XSSFBox xssfBox = new XSSFBox(tableName);

        //build arrays of a report data by database values
        String[][] reportData = TableReportDBInstantiation.requireDataArrays(tableName);

        putDataInSheet(xssfBox, reportData);
        return xssfBox.workbook;
    }

    static void writeReportFile(XSSFWorkbook workbook) {
        File file = new File(PATH_FOR_REPORTS + workbook.getSheetName(0).toLowerCase() + fileFormat);
        try(FileOutputStream fileOutput = new FileOutputStream(file)) {
            workbook.write(fileOutput);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String[] buildTableColumns(Account account) {
        String[] productTypes = account.recordManager.productMap.getAllTitles();
        String[] columns = new String[productTypes.length + 2];
        columns[0] = "patient";
        columns[1] = "clinic";
        int i = 2;
        for (String s : productTypes) {
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
            String[] tableRow = new String[columns.length];
            tableRow[0] = wr.getPatient();
            tableRow[1] = wr.getClinic();

            //looping product list if it exists
            if (!(wr.getProducts().isEmpty())) {
                for (Product p : wr.getProducts()) {

                    //search index of the product column
                    for (int i = 2; i < columns.length; i++) {
                        if (p.title().equals(columns[i])) {

                            //write quantity of the product items
                            tableRow[i] = String.valueOf(p.quantity());
                        } else {
                            if (tableRow[i] == null) {
                                tableRow[i] = "";
                            }
                        }
                    }
                }
            }
            result[r] = tableRow;
            r++;
        }
        return result;
    }

    private static void putDataInSheet(XSSFBox xssfBox, String[][] reportData) {

        for (int i = 0; i < reportData.length; i++) {
            xssfBox.row = xssfBox.sheet.createRow(i);

            //iterate each row and set it to the table
            for (int j = 0; j < reportData[i].length; j++) {
                Cell cell = xssfBox.row.createCell(j);
                cell.setCellValue(reportData[i][j]);
            }
        }
    }

    private static class XSSFBox {

        private final XSSFWorkbook workbook;
        private final XSSFSheet sheet;
        private XSSFRow row;

        private XSSFBox(String tableName) {
            this.workbook = new XSSFWorkbook();
            this.sheet = workbook.createSheet(tableName);
        }
    }
}
