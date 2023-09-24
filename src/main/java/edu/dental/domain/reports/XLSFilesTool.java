package edu.dental.domain.reports;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class XLSFilesTool implements IFileTool {

    private static final String fileFormat = ".xlsx";

    private static final String PATH_FOR_REPORTS = "src/main/resources/";


    private final String tableName;
    private final String[][] reportData;
    private XSSFWorkbook workbook;

    public XLSFilesTool(String tableName, String[][] reportData) {
        this.tableName = tableName;
        this.reportData = reportData;
    }

    @Override
    public IFileTool createFile() {
        XSSFBox xssfBox = new XSSFBox(tableName);
        putDataInSheet(xssfBox, reportData);
        this.workbook = xssfBox.workbook;
        return this;
    }

    @Override
    public boolean writeFile() {
        if (this.workbook == null) {
            throw new NullPointerException("XSSFWorkbook object is null");
        }
        File file = new File(PATH_FOR_REPORTS + workbook.getSheetName(0).toLowerCase() + fileFormat);
        try(FileOutputStream fileOutput = new FileOutputStream(file)) {
            workbook.write(fileOutput);
            return true;
        } catch (IOException e) {
            //TODO logger
            e.printStackTrace();
            return false;
        }
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
