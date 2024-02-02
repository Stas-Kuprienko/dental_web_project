package edu.dental.service.control;

import edu.dental.APIResponseException;
import edu.dental.WebAPIManager;
import jakarta.servlet.http.HttpSession;

public interface Administrator {

    void signUp(HttpSession session, String name, String email, String password) throws APIResponseException;

    void signIn(HttpSession session, String email, String password) throws APIResponseException;

    DentalWorksService getDentalWorksService();

    ProductMapService getProductMapService();

    static Administrator getInstance() {
        return WebAPIManager.INSTANCE.getAdministrator();
    }
}
