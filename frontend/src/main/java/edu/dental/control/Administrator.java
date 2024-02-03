package edu.dental.control;

import edu.dental.APIResponseException;
import edu.dental.service.WebAPIManager;
import edu.dental.beans.UserBean;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

public interface Administrator {

    void signUp(HttpSession session, String name, String email, String password) throws APIResponseException;

    void signIn(HttpSession session, String email, String password) throws APIResponseException;

    UserBean getUser(String token) throws IOException, APIResponseException;

    UserBean updateUser(String token, String field, String value) throws IOException, APIResponseException;

    DentalWorksListService getDentalWorksListService();

    DentalWorkService getDentalWorksService();

    ProductMapService getProductMapService();

    static Administrator getInstance() {
        return WebAPIManager.INSTANCE.getAdministrator();
    }
}
