package edu.dental.service.my_repository;

import edu.dental.domain.authentication.AuthenticationException;
import edu.dental.domain.authentication.Authenticator;
import edu.dental.domain.records.WorkRecordBook;
import edu.dental.domain.records.WorkRecordBookException;
import edu.dental.dto.DentalWork;
import edu.dental.entities.User;
import edu.dental.service.Repository;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public final class MyRepository implements Repository {

    private MyRepository() {
        RAM = new ConcurrentHashMap<>();
    }


    private final ConcurrentHashMap<String, Account> RAM;

    public Account put(User user, WorkRecordBook recordBook) {
        Account account = new Account(user, recordBook);
        RAM.put(user.getEmail(), account);
        return account;
    }

    @Override
    public User logIn(String login, String password) throws AuthenticationException {
        User user;
        Account account = RAM.get(login);
        if (account != null) {
            user = account.user;
            if (Authenticator.verification(user, password)) {
                return account.user;
            }
            throw new AuthenticationException(AuthenticationException.Causes.PASS);
        } else {
            user = Authenticator.authenticate(login, password);
            WorkRecordBook recordBook;
            try {
                recordBook = WorkRecordBook.getInstance(user.getId());
            } catch (WorkRecordBookException e) {
                throw new AuthenticationException(AuthenticationException.Causes.ERROR);
            }
            put(user, recordBook);
            return user;
        }
    }

    @Override
    public Account signUp(String name, String login, String password) throws AuthenticationException {
        User user = Authenticator.create(name, login, password);
        WorkRecordBook recordBook = WorkRecordBook.createNew(user.getId());
        return put(user, recordBook);
    }

    @Override
    public List<DentalWork> getDentalWorkDtoList(String user) {
        return get(user).recordBook.getRecords().stream().map(DentalWork::new).toList();
    }

    @Override
    public User getUser(String login) {
        return RAM.get(login).user;
    }

    @Override
    public WorkRecordBook getRecordBook(String login) {
        return RAM.get(login).recordBook;
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
