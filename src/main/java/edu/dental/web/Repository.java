package edu.dental.web;

import edu.dental.domain.APIManager;
import edu.dental.domain.authentication.AuthenticationException;
import edu.dental.domain.entities.User;
import edu.dental.domain.entities.dto.DentalWorkDTO;
import edu.dental.domain.records.WorkRecordBook;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

public interface Repository {

    static Repository getInstance() {
        return APIManager.INSTANCE.getRepository();
    }

    void put(User user, WorkRecordBook recordBook);

    User logIn(String login, String password) throws AuthenticationException;

    User signUp(String name, String login, String password) throws AuthenticationException;

    Account get(String login);

    void setDtoAttributes(HttpServletRequest request, String user);

    List<DentalWorkDTO> getDentalWorkDtoList(String user);

    User getUser(String login);

    WorkRecordBook getRecordBook(String login);

    void delete(String login);


    interface Account {
        User user();
        WorkRecordBook recordBook();
    }
}
