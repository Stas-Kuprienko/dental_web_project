package edu.dental.service.control;

import edu.dental.APIResponseException;
import edu.dental.beans.DentalWork;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

public interface DentalWorksService {

    DentalWork createWork(HttpSession session, String patient, String clinic, String product, int quantity, String complete) throws IOException, APIResponseException;

    DentalWork updateDentalWork(HttpSession session, int id, String field, String value, String quantity) throws IOException, APIResponseException, ServletException;

    DentalWork getDentalWorkById(HttpSession session, int id) throws IOException, APIResponseException;

    void updateDentalWorkList(HttpSession session, DentalWork dw);

    void deleteDentalWorkFromList(HttpSession session, int id) throws IOException, APIResponseException;

    DentalWork removeProductFromDentalWork(HttpSession session, int id, String product) throws IOException, APIResponseException;
}
