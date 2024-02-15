package edu.dental.control;

import edu.dental.HttpWebException;
import edu.dental.service.WebAPIManager;
import edu.dental.beans.UserBean;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

public interface Administrator {

    void signUp(HttpSession session, String name, String email, String password) throws HttpWebException;

    void signIn(HttpSession session, String email, String password) throws HttpWebException;

    UserBean getUser(String token) throws IOException, HttpWebException;

    UserBean updateUser(String token, String field, String value) throws IOException, HttpWebException;

    DentalWorksListService getDentalWorksListService();

    DentalWorkService getDentalWorksService();

    ProductMapService getProductMapService();

    ProfitRecordService getProfitRecordService();

    static Administrator getInstance() {
        return WebAPIManager.INSTANCE.getAdministrator();
    }
}
