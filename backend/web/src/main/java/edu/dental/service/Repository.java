package edu.dental.service;

import edu.dental.WebAPI;
import edu.dental.domain.authentication.AuthenticationException;
import edu.dental.entities.User;
import edu.dental.dto.DentalWork;
import edu.dental.domain.records.WorkRecordBook;
import edu.dental.dto.ProductMap;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

public interface Repository {

    static Repository getInstance() {
        return WebAPI.INSTANCE.getRepository();
    }

    void put(User user, WorkRecordBook recordBook);

    User logIn(String login, String password) throws AuthenticationException;

    User signUp(String name, String login, String password) throws AuthenticationException;

    Account get(String login);

    void setDtoAttributes(HttpServletRequest request, String user);

    ProductMap getMapDto(String user);

    List<DentalWork> getDentalWorkDtoList(String user);

    User getUser(String login);

    WorkRecordBook getRecordBook(String login);

    void delete(String login);


    interface Account {
        User user();
        WorkRecordBook recordBook();
    }
}
