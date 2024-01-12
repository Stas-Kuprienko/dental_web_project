package edu.dental.domain.reports;

import java.io.OutputStream;

public interface SheetFileTool {

    <T> void addSheet(T[][] sheetData);

    boolean writeFile() throws ReportServiceException;

    boolean writeFile(String fileName) throws ReportServiceException;

    OutputStream writeFile(OutputStream output) throws ReportServiceException;
}
