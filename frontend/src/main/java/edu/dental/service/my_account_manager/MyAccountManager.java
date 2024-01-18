package edu.dental.service.my_account_manager;

import edu.dental.APIResponseException;
import edu.dental.WebUtility;
import edu.dental.beans.DentalWork;
import edu.dental.beans.ProductMap;
import edu.dental.service.AccountManager;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MyAccountManager implements AccountManager {

    private static final String dentalWorkListUrl = "main/dental-works";
    private static final String productMapUrl = "main/product-map";


    @Override
    public void setWorkList(HttpSession session) throws IOException, APIResponseException {
        String token = (String) session.getAttribute(WebUtility.INSTANCE.sessionToken);

        String jsonWorks = WebUtility.INSTANCE.requestSender().sendHttpGetRequest(token, dentalWorkListUrl);
        DentalWork[] works = WebUtility.INSTANCE.parseFromJson(jsonWorks, DentalWork[].class);

        session.setAttribute(WebUtility.INSTANCE.sessionWorks, works);
    }

    @Override
    public void setProductMap(HttpSession session) throws IOException, APIResponseException {
        String token = (String) session.getAttribute(WebUtility.INSTANCE.sessionToken);

        String jsonMap = WebUtility.INSTANCE.requestSender().sendHttpGetRequest(token, productMapUrl);
        ProductMap.Item[] items = WebUtility.INSTANCE.parseFromJson(jsonMap, ProductMap.Item[].class);

        ProductMap map = new ProductMap(items);
        session.setAttribute(WebUtility.INSTANCE.sessionMap, map);
    }

    @Override
    public void createWork(HttpServletRequest request) {

    }

    @Override
    public void createProductItem(HttpServletRequest request) {

    }

    @Override
    public void updateWork(HttpSession session, DentalWork dw) {
        DentalWork[] works = (DentalWork[]) session.getAttribute(WebUtility.INSTANCE.sessionWorks);
        ArrayList<DentalWork> workList = new ArrayList<>(List.of(works));

        workList.stream().filter(e -> e.getId() == dw.getId()).findAny().ifPresent(workList::remove);
        workList.add(dw);

        works = workList.toArray(works);
        session.setAttribute(WebUtility.INSTANCE.sessionWorks, works);
    }

    @Override
    public void updateProductItem(HttpServletRequest request) {

    }

    @Override
    public void deleteWork(HttpServletRequest request) {

    }

    @Override
    public void deleteProductItem(HttpServletRequest request) {

    }
}
