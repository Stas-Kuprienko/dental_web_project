package edu.dental.service;

import edu.dental.APIResponseException;
import edu.dental.beans.DentalWork;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

public interface AccountManager {

    void setWorkList(HttpSession session) throws IOException, APIResponseException;

    void setProductMap(HttpSession session) throws IOException, APIResponseException;

    void createWork(HttpServletRequest request);

    void createProductItem(HttpServletRequest request);

    void updateWork(HttpSession session, DentalWork dw);

    void updateProductItem(HttpServletRequest request);

    void deleteWork(HttpServletRequest request);

    void deleteProductItem(HttpServletRequest request);
}
