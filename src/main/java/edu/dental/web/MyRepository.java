package edu.dental.web;

import edu.dental.database.DBService;
import edu.dental.database.DatabaseException;
import edu.dental.domain.APIManager;
import edu.dental.domain.authentication.AuthenticationException;
import edu.dental.domain.authentication.Authenticator;
import edu.dental.domain.entities.I_DentalWork;
import edu.dental.domain.entities.User;
import edu.dental.domain.records.ProductMap;
import edu.dental.domain.records.WorkRecordBook;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public final class MyRepository implements Repository {

    private static final Repository repo;

    private MyRepository() {
        RAM = new ConcurrentHashMap<>();
    }

    static {
        repo = new MyRepository();
    }


    private final ConcurrentHashMap<String, Account> RAM;

    public void put(User user, WorkRecordBook recordBook) {
        Account account = new Account(user, recordBook);
        RAM.put(user.getEmail(), account);
    }

    @Override
    public User find(String login, String password) throws AuthenticationException, DatabaseException {
        User user;
        Account acc = RAM.get(login);
        if (acc != null) {
            return acc.user;
        } else {
            user = Authenticator.authenticate(login, password);
            WorkRecordBook recordBook;
            DBService dbService = APIManager.instance().getDBService();
            List<I_DentalWork> works = dbService.getDentalWorkDAO(user).getAll();
            ProductMap map = dbService.getProductMapDAO(user).get();
            recordBook = APIManager.instance().getWorkRecordBook(works, map);
            put(user, recordBook);
            return user;
        }
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

    public static synchronized Repository instance() {
        return repo;
    }

    public record Account(User user, WorkRecordBook recordBook) implements Repository.Account {}
}
