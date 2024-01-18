package edu.dental.service.my_account_manager;

import edu.dental.APIResponseException;
import edu.dental.WebUtility;
import edu.dental.beans.DentalWork;
import edu.dental.service.DentalWorksService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MyDentalWorksService implements DentalWorksService {

    private static final String dentalWorkListUrl = "main/dental-works";
    private static final String dentalWorkUrl = "main/dental-work";
    private static final String idParam = "id";
    private static final String patientParam = "patient";
    private static final String clinicParam = "clinic";
    private static final String productParam = "product";
    private static final String quantityParam = "quantity";
    private static final String completeParam = "complete";
    private static final String fieldParam = "field";
    private static final String valueParam = "value";


    @Override
    public void setWorkList(HttpSession session) throws IOException, APIResponseException {
        String token = (String) session.getAttribute(WebUtility.INSTANCE.sessionToken);

        String jsonWorks = WebUtility.INSTANCE.requestSender().sendHttpGetRequest(token, dentalWorkListUrl);
        DentalWork[] works = WebUtility.INSTANCE.parseFromJson(jsonWorks, DentalWork[].class);

        session.setAttribute(WebUtility.INSTANCE.sessionWorks, works);
    }

    @Override
    public void createWork(HttpServletRequest request) {

    }

    @Override
    public void updateWork(HttpServletRequest request) throws IOException, APIResponseException {
        HttpSession session = request.getSession();
        String jwt = (String) session.getAttribute(WebUtility.INSTANCE.sessionToken);

        int id = Integer.parseInt(request.getParameter(idParam));
        String field = request.getParameter(fieldParam);
        String value = request.getParameter(valueParam);
        WebUtility.QueryFormer queryFormer = new WebUtility.QueryFormer();
        queryFormer.add(idParam, id);
        queryFormer.add(fieldParam, field);
        queryFormer.add(valueParam, value);
        if (field.equals(productParam)) {
            if (!(value == null || value.isEmpty())) {
                int quantity = Integer.parseInt(request.getParameter(quantityParam));
                queryFormer.add(quantityParam, quantity);
            } else {
                throw new NullPointerException();
            }
        }
        String requestParam = queryFormer.form();
        WebUtility.INSTANCE.requestSender().sendHttpPutRequest(jwt, dentalWorkUrl + '/' + id, requestParam);

//        if (dentalWork.getReportId() == 0) {
//            updateDentalWorks(session, dentalWork);
//        }
//        request.setAttribute("work", dentalWork);
    }

    @Override
    public void deleteWork(HttpServletRequest request) {

    }

    private void updateDentalWorks(HttpSession session, DentalWork dw) {
        DentalWork[] works = (DentalWork[]) session.getAttribute(WebUtility.INSTANCE.sessionWorks);
        ArrayList<DentalWork> workList = new ArrayList<>(List.of(works));

        workList.stream().filter(e -> e.getId() == dw.getId()).findAny().ifPresent(workList::remove);
        workList.add(dw);

        works = workList.toArray(works);
        session.setAttribute(WebUtility.INSTANCE.sessionWorks, works);
    }

    private DentalWork changeFieldValue(int id, String field, String value) {
        return null;
    }
}
