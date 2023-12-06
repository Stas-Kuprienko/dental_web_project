package edu.dental.web.my_repository;

import edu.dental.domain.authentication.AuthenticationException;
import edu.dental.domain.authentication.Authenticator;
import edu.dental.domain.entities.DentalWork;
import edu.dental.domain.entities.User;
import edu.dental.domain.entities.dto.DentalWorkDTO;
import edu.dental.domain.entities.dto.ProductMapDTO;
import edu.dental.domain.records.WorkRecordBook;
import edu.dental.domain.records.WorkRecordBookException;
import edu.dental.web.Repository;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public final class MyRepository implements Repository {

    private MyRepository() {
        RAM = new ConcurrentHashMap<>();
    }


    private final ConcurrentHashMap<String, Account> RAM;

    public void put(User user, WorkRecordBook recordBook) {
        Account account = new Account(user, recordBook);
        RAM.put(user.getEmail(), account);
    }

    @Override
    public User logIn(String login, String password) throws AuthenticationException {
        User user;
        Account acc = RAM.get(login);
        if (acc != null) {
            user = acc.user;
            if (Authenticator.verification(user, password)) {
                return acc.user;
            }
            throw new AuthenticationException(AuthenticationException.Causes.PASS);
        } else {
            user = Authenticator.authenticate(login, password);
            WorkRecordBook recordBook;
            try {
                recordBook = WorkRecordBook.getInstance(user);
            } catch (WorkRecordBookException e) {
                throw new AuthenticationException(e);
            }
            put(user, recordBook);
            return user;
        }
    }

    @Override
    public User signUp(String name, String login, String password) throws AuthenticationException {
        return Authenticator.create(name, login, password);
    }

    @Override
    public void setDtoAttributes(HttpServletRequest request, String user) {
        Account account = get(user);
        ProductMapDTO map = new ProductMapDTO(account.recordBook.getMap());
        List<DentalWork> list = account.recordBook.getList();
        DentalWorkDTO[] works = new DentalWorkDTO[list.size()];
        list.stream().map(DentalWorkDTO::new).toList().toArray(works);
        request.setAttribute("map", map);
        request.setAttribute("works", works);
    }


    @Override
    public User getUser(String login) {
        Account account = RAM.get(login);
        if (account == null) {
            throw new NullPointerException("The user is not found.");
        }
        return account.user;
    }

    @Override
    public WorkRecordBook getRecordBook(String login) {
        Account account = RAM.get(login);
        if (account == null) {
            throw new NullPointerException("The user is not found.");
        }
        return account.recordBook;
    }

    @Override
    public void delete(String login) {
        RAM.remove(login);
    }

    @Override
    public Account get(String login) {
        return RAM.get(login);
    }

    public record Account(User user, WorkRecordBook recordBook) implements Repository.Account {}
}
