package edu.dental.service;

import edu.dental.WebAPI;
import edu.dental.database.DatabaseException;
import edu.dental.domain.records.WorkRecordBook;
import edu.dental.dto.DentalWorkDto;
import edu.dental.dto.ProductMapDto;
import edu.dental.dto.UserDto;
import edu.dental.entities.User;
import edu.dental.security.WebSecurityException;
import edu.dental.service.lifecycle.Monitorable;

import java.util.List;

public interface Repository {

    String paramUser = "user";

    static Repository getInstance() {
        return WebAPI.INSTANCE.getRepository();
    }

    void startMonitor();

    void updateAccountLastAction(int userId);

    User createNew(String name, String login, byte[] password) throws DatabaseException;

    boolean put(User user);

    List<DentalWorkDto> getDentalWorkDtoList(int id);

    ProductMapDto getProductMapDto(int id);

    User getUser(int id);

    WorkRecordBook getRecordBook(int id);

    UserDto update(int userId, String field, String value) throws DatabaseException;

    void delete(int id);

    void delete(int id, boolean fromDatabase) throws DatabaseException;

    void reloadWorks(int id) throws DatabaseException;

    void ensureLoggingIn(int id) throws WebSecurityException, DatabaseException;


    abstract class Account extends Monitorable {

        protected Account(int key) {
            super(key);
        }

        abstract public User user();
        abstract public WorkRecordBook recordBook();
    }
}
