package edu.dental.domain.reports.my_report_service;

import edu.dental.domain.reports.IFileTool;
import edu.dental.domain.reports.ReportServiceException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;

class XLSXFilesTool implements IFileTool {

    public static final String fileFormat = ".xlsx";

    private static final String PATH_FOR_REPORTS = "src/main/resources/";


    private final String[][] reportData;
    private final XSSFBox xssfBox;

    XLSXFilesTool(String tableName, String[][] reportData) {
        this.reportData = reportData;
        this.xssfBox = new XSSFBox(tableName);
    }

    XLSXFilesTool(String[][] reportData) {
        this.reportData = reportData;
        this.xssfBox = new XSSFBox();
    }


    @Override
    public IFileTool createFile() {
        putDataInSheet(xssfBox, reportData);
        return this;
    }

    @Override
    public boolean writeFile() throws ReportServiceException {
        File file = new File(PATH_FOR_REPORTS + xssfBox.workbook.getSheetName(0).toLowerCase() + fileFormat);
        try(FileOutputStream fileOutput = new FileOutputStream(file); xssfBox) {
            xssfBox.workbook.write(fileOutput);
            return true;
        } catch (Exception e) {
            //TODO logger
            throw new ReportServiceException(e.getMessage(), e);
        }
    }

    @Override
    public OutputStream writeFile(OutputStream output) throws ReportServiceException {
        try (xssfBox) {
            xssfBox.workbook.write(output);
        } catch (Exception e) {
            throw new ReportServiceException(e.getMessage(), e);
        }
        return output;
    }

    private void putDataInSheet(XSSFBox xssfBox, String[][] reportData) {

        for (int i = 0; i < reportData.length; i++) {
            xssfBox.row = xssfBox.sheet.createRow(i);

            //iterate each row and set it to the table
            for (int j = 0; j < reportData[i].length; j++) {
                Cell cell = xssfBox.row.createCell(j);
                cell.setCellValue(reportData[i][j]);
            }
        }
    }

    private static class XSSFBox implements AutoCloseable {

        private final XSSFWorkbook workbook;
        private final XSSFSheet sheet;
        private XSSFRow row;

        private XSSFBox() {
            this.workbook = new XSSFWorkbook();
            this.sheet = workbook.createSheet();
        }

        private XSSFBox(String tableName) {
            this.workbook = new XSSFWorkbook();
            this.sheet = workbook.createSheet(tableName);
        }

        @Override
        public void close() throws Exception {
            this.workbook.close();
        }
    }
}
