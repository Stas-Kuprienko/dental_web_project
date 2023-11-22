package edu.dental.web;

import edu.dental.domain.entities.User;
import edu.dental.domain.records.WorkRecordBook;

import java.util.HashMap;

public final class RAM {

    private RAM() {}
    static {
        repo = new HashMap<>();
    }

    public static void put(User user, WorkRecordBook recordBook) {
        Account account = new Account(user, recordBook);
        repo.put(user.getEmail(), account);
    }

    public static User getUser(String email) {
        Account account = repo.get(email);
        if (account == null) {
            throw new NullPointerException("The user is not found.");
        }        return account.user;
    }

    public static WorkRecordBook getWorkRecordBook(String email) {
        Account account = repo.get(email);
        if (account == null) {
            throw new NullPointerException("The user is not found.");
        }
        return account.recordBook;
    }

    public static Account get(String email) {
        Account account = repo.get(email);
        if (account == null) {
            throw new NullPointerException("The user is not found.");
        }
        return account;
    }

    private static final HashMap<String, Account> repo;

    public record Account(User user, WorkRecordBook recordBook) {}
}
