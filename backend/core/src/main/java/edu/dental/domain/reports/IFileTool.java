package edu.dental.domain.reports;

import java.io.OutputStream;

public interface IFileTool {

    IFileTool createFile();

    boolean writeFile() throws ReportServiceException;

    OutputStream writeFile(OutputStream output) throws ReportServiceException;
}
