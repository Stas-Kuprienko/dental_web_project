package edu.dental.domain.reports.my_report_service;

import edu.dental.domain.reports.SheetFileTool;
import edu.dental.domain.reports.ReportException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.time.LocalDate;

class XLSXFileTool implements SheetFileTool {

    public static final String fileFormat = ".xlsx";

    private static final String PATH_FOR_REPORTS = "src/main/resources/";

    private final XSSFBox xssfBox;

    XLSXFileTool() {
        this.xssfBox = new XSSFBox();
    }


    @Override
    public <T> void addSheet(T[][] sheetData) {
        String[][] sheetDataStrArr = (String[][]) sheetData;
        putDataInSheet(sheetDataStrArr);
    }

    @Override
    public boolean writeFile() throws ReportException {
        String defaultFileName = "new_file_" + LocalDate.now();
        return writeFile(defaultFileName);
    }

    @Override
    public boolean writeFile(String fileName) throws ReportException {
        File file = new File(PATH_FOR_REPORTS + fileName + fileFormat);
        try(FileOutputStream fileOutput = new FileOutputStream(file); xssfBox) {
            xssfBox.workbook.write(fileOutput);
            return true;
        } catch (Exception e) {
            throw new ReportException(e);
        }
    }

    @Override
    public OutputStream writeFile(OutputStream output) throws ReportException {
        try (xssfBox) {
            xssfBox.workbook.write(output);
        } catch (Exception e) {
            throw new ReportException(e);
        }
        return output;
    }

    private void putDataInSheet(String[][] sheetData) {
        XSSFSheet sheet = xssfBox.createSheet();

        for (int i = 0; i < sheetData.length; i++) {
            XSSFRow row = sheet.createRow(i);

            //iterate each row and set it to the table
            for (int j = 0; j < sheetData[i].length; j++) {
                Cell cell = row.createCell(j);
                cell.setCellValue(sheetData[i][j]);
            }
        }
    }

    private static class XSSFBox implements AutoCloseable {

        private final XSSFWorkbook workbook;

        private XSSFBox() {
            this.workbook = new XSSFWorkbook();
        }

        private XSSFSheet createSheet(String listName) {
            return this.workbook.createSheet(listName);
        }

        private XSSFSheet createSheet() {
            return this.workbook.createSheet();
        }

        @Override
        public void close() throws Exception {
            this.workbook.close();
        }
    }
}
