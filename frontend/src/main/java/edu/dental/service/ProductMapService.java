package edu.dental.service;

import edu.dental.APIResponseException;
import edu.dental.WebAPIManager;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

public interface ProductMapService {

    static ProductMapService getInstance() {
        return WebAPIManager.INSTANCE.getProductMapService();
    }

    void setProductMap(HttpSession session) throws IOException, APIResponseException;

    void createProductItem(HttpServletRequest request);

    void updateProductItem(HttpServletRequest request);

    void deleteProductItem(HttpServletRequest request);
}
