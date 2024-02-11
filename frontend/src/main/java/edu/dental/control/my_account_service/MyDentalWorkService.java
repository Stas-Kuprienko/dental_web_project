package edu.dental.control.my_account_service;

import edu.dental.APIResponseException;
import edu.dental.beans.DentalWork;
import edu.dental.beans.Product;
import edu.dental.control.DentalWorkService;
import edu.dental.service.WebUtility;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MyDentalWorkService implements DentalWorkService {

    private static final String dentalWorkUrl = "main/dental-work";
    private static final String productParam = "product";
    private static final String quantityParam = "quantity";
    private static final String fieldParam = "field";
    private static final String valueParam = "value";

    private final WebUtility.HttpRequestSender httpRequestSender;

    MyDentalWorkService() {
        this.httpRequestSender = WebUtility.INSTANCE.requestSender();
    }


    @Override
    public DentalWork createWork(HttpSession session, String patient, String clinic, String product, int quantity, String complete) throws IOException, APIResponseException {
        Product[] products;
        if (product == null || product.isEmpty()) {
            products = new Product[]{};
        } else {
            Product p = new Product(0, product, (byte) quantity, 0);
            products = new Product[]{p};
        }
        DentalWork dw = new DentalWork(0, patient, clinic, products, null, complete, null, "MAKE", 0);

        String newToJson = WebUtility.INSTANCE.parseToJson(dw);
        String token = (String) session.getAttribute(WebUtility.INSTANCE.attribToken);
        String returnedJson = httpRequestSender.sendHttpPostRequest(token, dentalWorkUrl, newToJson);
        DentalWork dentalWork = WebUtility.INSTANCE.parseFromJson(returnedJson, DentalWork.class);
        DentalWork[] works = (DentalWork[]) session.getAttribute(WebUtility.INSTANCE.attribWorks);
        works = addToList(works, dentalWork);
        session.setAttribute(WebUtility.INSTANCE.attribWorks, works);
        return dentalWork;
    }

    @Override
    public DentalWork updateDentalWork(HttpSession session, int id, String field, String value, String quantity) throws APIResponseException, IOException {
        String jwt = (String) session.getAttribute(WebUtility.INSTANCE.attribToken);

        WebUtility.QueryFormer queryFormer = new WebUtility.QueryFormer();
        queryFormer.add(fieldParam, field);
        queryFormer.add(valueParam, value);
        if (field.equals(productParam)) {
            if (!(quantity == null || quantity.isEmpty())) {
                queryFormer.add(quantityParam, quantity);
            } else {
                NullPointerException e = new NullPointerException("parameter is null");
                throw new APIResponseException(APIResponseException.ERROR.BAD_REQUEST, e.getStackTrace());
            }
        }
        String requestParam = queryFormer.form();

        String json = httpRequestSender.sendHttpPutRequest(jwt, dentalWorkUrl + '/' + id, requestParam);
        DentalWork dw = WebUtility.INSTANCE.parseFromJson(json, DentalWork.class);

        if (dw.getReportId() == 0) {
            updateDentalWorkList(session, dw);
        }
        return dw;
    }

    @Override
    public DentalWork getDentalWorkById(HttpSession session, int id) throws IOException, APIResponseException {
        DentalWork dw;
        DentalWork[] works = (DentalWork[]) session.getAttribute(WebUtility.INSTANCE.attribWorks);
        int i = getIndexById(works, id);
        if (i < 0) {
            String jwt = (String) session.getAttribute(WebUtility.INSTANCE.attribToken);
            String json = httpRequestSender.sendHttpGetRequest(jwt, dentalWorkUrl + "/" + id);
            dw = WebUtility.INSTANCE.parseFromJson(json, DentalWork.class);
        } else {
            dw = works[i];
        }
        return dw;
    }

    @Override
    public void updateDentalWorkList(HttpSession session, DentalWork dw) {
        DentalWork[] works = (DentalWork[]) session.getAttribute(WebUtility.INSTANCE.attribWorks);
        ArrayList<DentalWork> workList = new ArrayList<>(List.of(works));

        int i = getIndexById(works, dw.getId());
        if (i < 0) {
            workList.add(dw);
        } else {
            workList.set(i, dw);
        }
        works = workList.toArray(works);
        session.setAttribute(WebUtility.INSTANCE.attribWorks, works);
    }

    @Override
    public void deleteDentalWorkFromList(HttpSession session, int id) throws IOException, APIResponseException {
        String token = (String) session.getAttribute(WebUtility.INSTANCE.attribToken);
        httpRequestSender.sendHttpDeleteRequest(token, dentalWorkUrl + '/' + id, null);

        DentalWork[] works = (DentalWork[]) session.getAttribute(WebUtility.INSTANCE.attribWorks);
        int i = getIndexById(works, id);
        if (i >= 0) {
            ArrayList<DentalWork> workList = new ArrayList<>(List.of(works));
            workList.remove(i);
            works = workList.toArray(new DentalWork[]{});
            session.setAttribute(WebUtility.INSTANCE.attribWorks, works);
        }
    }

    @Override
    public DentalWork removeProductFromDentalWork(HttpSession session, int id, String product) throws IOException, APIResponseException {
        String token = (String) session.getAttribute(WebUtility.INSTANCE.attribToken);

        String parameter = productParam + '=' + product;
        String json = httpRequestSender.sendHttpDeleteRequest(token, dentalWorkUrl + '/' + id, parameter);

        DentalWork dw = WebUtility.INSTANCE.parseFromJson(json, DentalWork.class);
        updateDentalWorkList(session, dw);
        return dw;
    }

    private DentalWork[] addToList(DentalWork[] works, DentalWork dentalWork) {
        ArrayList<DentalWork> dentalWorkList = new ArrayList<>(List.of(works));
        dentalWorkList.add(dentalWork);
        return dentalWorkList.toArray(new DentalWork[]{});
    }

    private int getIndexById(DentalWork[] works, int id) {
        return Arrays.binarySearch(works, new DentalWork(id));
    }
}