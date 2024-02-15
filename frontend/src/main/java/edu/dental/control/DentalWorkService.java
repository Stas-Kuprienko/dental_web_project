package edu.dental.control;

import edu.dental.HttpWebException;
import edu.dental.beans.DentalWork;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

public interface DentalWorkService {

    DentalWork createWork(HttpSession session, String patient, String clinic, String product, int quantity, String complete) throws IOException, HttpWebException;

    DentalWork updateDentalWork(HttpSession session, int id, String field, String value, String quantity) throws IOException, HttpWebException, ServletException;

    DentalWork getDentalWorkById(HttpSession session, int id) throws IOException, HttpWebException;

    void updateDentalWorkList(HttpSession session, DentalWork dw);

    void deleteDentalWorkFromList(HttpSession session, int id) throws IOException, HttpWebException;

    DentalWork removeProductFromDentalWork(HttpSession session, int id, String product) throws IOException, HttpWebException;
}
