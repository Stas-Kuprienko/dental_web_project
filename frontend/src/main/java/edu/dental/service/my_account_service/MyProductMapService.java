package edu.dental.service.my_account_service;

import edu.dental.APIResponseException;
import edu.dental.service.WebUtility;
import edu.dental.beans.ProductMap;
import edu.dental.service.ProductMapService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

public class MyProductMapService implements ProductMapService {

    private static final String productMapUrl = "main/product-map";


    @Override
    public void setProductMap(HttpSession session) throws IOException, APIResponseException {
        String token = (String) session.getAttribute(WebUtility.INSTANCE.sessionToken);

        String jsonMap = WebUtility.INSTANCE.requestSender().sendHttpGetRequest(token, productMapUrl);
        ProductMap.Item[] items = WebUtility.INSTANCE.parseFromJson(jsonMap, ProductMap.Item[].class);

        ProductMap map = new ProductMap(items);
        session.setAttribute(WebUtility.INSTANCE.sessionMap, map);
    }

    @Override
    public void createProductItem(HttpServletRequest request) {

    }

    @Override
    public void updateProductItem(HttpServletRequest request) {

    }

    @Override
    public void deleteProductItem(HttpServletRequest request) {

    }
}
