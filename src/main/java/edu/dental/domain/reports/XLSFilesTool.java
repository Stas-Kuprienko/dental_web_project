package edu.dental.domain.reports;

import edu.dental.domain.entities.User;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

class XLSFilesTool {

    private XLSFilesTool() {}

    private static final String fileFormat = ".xlsx";

    private static final String PATH_FOR_REPORTS = "src/main/resources/";

    /**
     * Create the file(.xls) with monthly report table.
     * @param user The {@link User} object that requires.
     * @param report The {@link MonthlyReport} object which needs to convert to a file.
     */
    static XSSFWorkbook createFileReport(String tableName, String[][] reportData) {
        XSSFBox xssfBox = new XSSFBox(tableName);
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
