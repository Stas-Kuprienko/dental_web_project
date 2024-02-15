package edu.dental.control;

import edu.dental.HttpWebException;
import edu.dental.beans.ProfitRecord;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

public interface ProfitRecordService {

    ProfitRecord[] get(HttpSession session, String year, String month) throws IOException, HttpWebException;

    ProfitRecord[] countCurrent(HttpSession session);

    ProfitRecord[] countAll(HttpSession session) throws IOException, HttpWebException;

    ProfitRecord[] countAnother(HttpSession session, String year, String month) throws IOException, HttpWebException;
}
