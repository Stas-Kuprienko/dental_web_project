package edu.dental.domain.reports;

import java.io.OutputStream;

public interface SheetFileTool {

    <T> void addSheet(T[][] sheetData);

    boolean writeFile() throws ReportException;

    boolean writeFile(String fileName) throws ReportException;

    OutputStream writeFile(OutputStream output) throws ReportException;
}
