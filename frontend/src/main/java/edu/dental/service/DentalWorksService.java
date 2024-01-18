package edu.dental.service;

import edu.dental.APIResponseException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

public interface DentalWorksService {

    static DentalWorksService getInstance() {
        return WebAPIManager.INSTANCE.getDentalWorksService();
    }

    void setWorkList(HttpSession session) throws IOException, APIResponseException;

    void createWork(HttpServletRequest request);

    void updateWork(HttpServletRequest request) throws IOException, APIResponseException;

    void deleteWork(HttpServletRequest request);

}
