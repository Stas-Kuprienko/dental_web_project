package edu.dental.web;

import edu.dental.database.DatabaseException;
import edu.dental.domain.authentication.AuthenticationException;
import edu.dental.domain.entities.User;
import edu.dental.domain.records.WorkRecordBook;

public interface Repository {

    void put(User user, WorkRecordBook recordBook);

    User logIn(String login, String password) throws AuthenticationException;

    User signUp(String name, String login, String password) throws DatabaseException;

    Account get(String login);

    User getUser(String login);

    WorkRecordBook getRecordBook(String login);

    void delete(String login);


    interface Account {
        User user();
        WorkRecordBook recordBook();
    }
}
