package edu.dental.web.my_repository;

import edu.dental.database.DatabaseService;
import edu.dental.database.DatabaseException;
import edu.dental.domain.APIManager;
import edu.dental.domain.authentication.AuthenticationException;
import edu.dental.domain.authentication.Authenticator;
import edu.dental.domain.entities.IDentalWork;
import edu.dental.domain.entities.User;
import edu.dental.domain.records.ProductMap;
import edu.dental.domain.records.WorkRecordBook;
import edu.dental.web.Repository;

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
            DatabaseService databaseService = APIManager.INSTANCE.getDatabaseService();
            List<IDentalWork> works;
            ProductMap map;
            try {
                map = databaseService.getProductMapDAO(user).get();
                works = databaseService.getDentalWorkDAO(user).getAll();
            } catch (DatabaseException e) {
                throw new AuthenticationException(e);
            }
            recordBook = APIManager.INSTANCE.getWorkRecordBook(works, map);
            put(user, recordBook);
            return user;
        }
    }

    @Override
    public User signUp(String name, String login, String password) throws DatabaseException {
        User user = new User(name, login, password);
        APIManager.INSTANCE.getDatabaseService().getUserDAO().put(user);
        WorkRecordBook recordBook = APIManager.INSTANCE.getWorkRecordBook();
        APIManager.INSTANCE.getRepository().put(user, recordBook);
        return user;
    }

    public User getUser(String login) {
        Account account = RAM.get(login);
        if (account == null) {
            throw new NullPointerException("The user is not found.");
        }
        return account.user;
    }

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

    public Account get(String login) {
        return RAM.get(login);
    }

    public record Account(User user, WorkRecordBook recordBook) implements Repository.Account {}
}
