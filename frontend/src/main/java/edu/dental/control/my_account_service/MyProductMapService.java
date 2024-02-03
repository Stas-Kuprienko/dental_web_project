package edu.dental.control.my_account_service;

import edu.dental.APIResponseException;
import edu.dental.beans.ProductMap;
import edu.dental.control.ProductMapService;
import edu.dental.service.WebUtility;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

public class MyProductMapService implements ProductMapService {

    private static final String productMapUrl = "main/product-map";
    private static final String titleParam = "title";
    private static final String priceParam = "price";

    MyProductMapService() {}


    @Override
    public void createProductItem(HttpSession session, String title, int price) throws IOException, APIResponseException {
        String jwt = (String) session.getAttribute(WebUtility.INSTANCE.attribToken);
        ProductMap.Item item;

        WebUtility.QueryFormer queryFormer = new WebUtility.QueryFormer();
        queryFormer.add(titleParam, title);
        queryFormer.add(priceParam, price);
        String requestParam = queryFormer.form();

        String json = WebUtility.INSTANCE.requestSender().sendHttpPostRequest(jwt, productMapUrl, requestParam);
        item = WebUtility.INSTANCE.parseFromJson(json, ProductMap.Item.class);
        ProductMap map = getProductMap(session);
        map.add(item);
        session.setAttribute(WebUtility.INSTANCE.attribMap, map.getItems());
    }

    @Override
    public void updateProductItem(HttpSession session, int id, String title, int price) throws IOException, APIResponseException {
        String jwt = (String) session.getAttribute(WebUtility.INSTANCE.attribToken);
        WebUtility.QueryFormer queryFormer = new WebUtility.QueryFormer();
        queryFormer.add(titleParam, title);
        queryFormer.add(priceParam, price);
        String requestParam = queryFormer.form();
        WebUtility.INSTANCE.requestSender().sendHttpPutRequest(jwt, productMapUrl + '/' + id, requestParam);
        ProductMap map = getProductMap(session);
        map.update(id, price);
        session.setAttribute(WebUtility.INSTANCE.attribMap, map.getItems());
    }

    @Override
    public void deleteProductItem(HttpSession session, int id, String title) throws IOException, APIResponseException {
        String jwt = (String) session.getAttribute(WebUtility.INSTANCE.attribToken);
        WebUtility.QueryFormer queryFormer = new WebUtility.QueryFormer();
        queryFormer.add(titleParam, title);
        WebUtility.INSTANCE.requestSender().sendHttpDeleteRequest(jwt, productMapUrl + '/' + id, queryFormer.form());
        ProductMap map = getProductMap(session);
        map.remove(id);
        session.setAttribute(WebUtility.INSTANCE.attribMap, map.getItems());
    }

    private ProductMap getProductMap(HttpSession session) {
        ProductMap.Item[] items = (ProductMap.Item[]) session.getAttribute(WebUtility.INSTANCE.attribMap);
        return new ProductMap(items);
    }
}
