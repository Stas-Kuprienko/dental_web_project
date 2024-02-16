package edu.dental.control;

import stas.exceptions.HttpWebException;
import edu.dental.beans.DentalWork;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

public interface DentalWorksListService {

    void getRequired(String year_month, HttpServletRequest request) throws IOException, HttpWebException;

    void sort(HttpSession session, int year, int month) throws IOException, HttpWebException;

    DentalWork[] setStatus(HttpSession session, int year, int month) throws IOException, HttpWebException;

    DentalWork[] search(String jwt, String patient, String clinic) throws IOException, HttpWebException;
}