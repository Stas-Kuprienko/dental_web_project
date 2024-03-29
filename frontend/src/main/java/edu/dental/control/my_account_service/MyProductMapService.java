package edu.dental.control.my_account_service;

import stas.exceptions.HttpWebException;
import stas.http_tools.HttpQueryFormer;
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
    public void createProductItem(HttpSession session, String title, int price) throws IOException, HttpWebException {
        String jwt = (String) session.getAttribute(WebUtility.INSTANCE.attribToken);
        ProductMap.Item item;

        HttpQueryFormer queryFormer = new HttpQueryFormer();
        queryFormer.add(titleParam, title);
        queryFormer.add(priceParam, price);
        String requestParam = queryFormer.form();

        String json = WebUtility.INSTANCE.getRequestSender().sendHttpPostRequest(jwt, productMapUrl, requestParam);
        item = WebUtility.INSTANCE.parseFromJson(json, ProductMap.Item.class);
        ProductMap map = getProductMap(session);
        map.add(item);
        session.setAttribute(WebUtility.INSTANCE.attribMap, map.getItems());
    }

    @Override
    public void updateProductItem(HttpSession session, int id, String title, int price) throws IOException, HttpWebException {
        String jwt = (String) session.getAttribute(WebUtility.INSTANCE.attribToken);
        HttpQueryFormer queryFormer = new HttpQueryFormer();
        queryFormer.add(titleParam, title);
        queryFormer.add(priceParam, price);
        String requestParam = queryFormer.form();
        WebUtility.INSTANCE.getRequestSender().sendHttpPutRequest(jwt, productMapUrl + '/' + id, requestParam);
        ProductMap map = getProductMap(session);
        map.update(id, price);
        session.setAttribute(WebUtility.INSTANCE.attribMap, map.getItems());
    }

    @Override
    public void deleteProductItem(HttpSession session, int id, String title) throws IOException, HttpWebException {
        String jwt = (String) session.getAttribute(WebUtility.INSTANCE.attribToken);
        HttpQueryFormer queryFormer = new HttpQueryFormer();
        queryFormer.add(titleParam, title);
        WebUtility.INSTANCE.getRequestSender().sendHttpDeleteRequest(jwt, productMapUrl + '/' + id, queryFormer.form());
        ProductMap map = getProductMap(session);
        map.remove(id);
        session.setAttribute(WebUtility.INSTANCE.attribMap, map.getItems());
    }

    private ProductMap getProductMap(HttpSession session) {
        ProductMap.Item[] items = (ProductMap.Item[]) session.getAttribute(WebUtility.INSTANCE.attribMap);
        return new ProductMap(items);
    }
}
