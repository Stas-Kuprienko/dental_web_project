package edu.dental.service.my_repository;

import edu.dental.database.DatabaseException;
import edu.dental.database.DatabaseService;
import edu.dental.database.dao.UserDAO;
import stas.utilities.UpdateFacility;
import edu.dental.domain.records.WorkRecordBook;
import edu.dental.dto.DentalWorkDto;
import edu.dental.dto.ProductMapDto;
import edu.dental.dto.UserDto;
import edu.dental.entities.User;
import edu.dental.security.AuthenticationService;
import edu.dental.service.Repository;
import edu.dental.service.lifecycle.LifecycleMonitor;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public final class MyRepository implements Repository {

    private final UserDAO userDAO;


    private MyRepository() {
        RAM = new ConcurrentHashMap<>();
        userDAO = DatabaseService.getInstance().getUserDAO();
    }

    private final ConcurrentHashMap<Integer, Account> RAM;


    @Override
    public void startMonitor() {
        LifecycleMonitor.INSTANCE.revision(RAM);
    }

    @Override
    public void updateAccountLastAction(int userId) {
        RAM.get(userId).updateLastAction();
    }

    @Override
    public User createNew(String name, String login, byte[] password) throws DatabaseException {
        User user = new User(name, login, password);
        try {
            userDAO.put(user);
            WorkRecordBook recordBook = WorkRecordBook.createNew(user.getId());
            Account account = new Account(user, recordBook);
            RAM.put(user.getId(), account);
            return user;
        } catch (DatabaseException e) {
            RAM.remove(user.getId());
            throw e;
        }
    }

    @Override
    public boolean put(User user) {
        try {
            WorkRecordBook recordBook = WorkRecordBook.getInstance(user.getId());
            Account account = new Account(user, recordBook);
            RAM.put(user.getId(), account);
            return true;
        } catch (DatabaseException e) {
            return false;
        }
    }

    @Override
    public List<DentalWorkDto> getDentalWorkDtoList(int id) {
        return RAM.get(id).recordBook.getRecords().stream().map(DentalWorkDto::new).toList();
    }

    @Override
    public ProductMapDto getProductMapDto(int id) {
        return new ProductMapDto(RAM.get(id).recordBook.getProductMap());
    }

    @Override
    public User getUser(int id) {
        return RAM.get(id).user();
    }

    @Override
    public WorkRecordBook getRecordBook(int id) {
        return RAM.get(id).recordBook();
    }

    @Override
    public UserDto update(int userId, String field, String value) throws DatabaseException {
        User user = getUser(userId);
        if (field.equals("password")) {
            if (!(AuthenticationService.getInstance().updatePassword(user, value))) {
                return null;
            }
        } else {
            UpdateFacility<User> updateFacility = new UpdateFacility<>();
            try {
                updateFacility.init(user, field);
                updateFacility.setNewValue(value);
                userDAO.update(user);
            } catch (DatabaseException e) {
                try {
                    updateFacility.revert();
                } catch (ReflectiveOperationException ignored) {}
                throw e;
            } catch (ReflectiveOperationException e) {
                return null;
            }
        }
        return new UserDto(user);
    }

    @Override
    public void delete(int id) {
        RAM.remove(id);
    }

    @Override
    public void delete(int id, boolean fromDatabase) throws DatabaseException {
        if (fromDatabase) {
            userDAO.delete(id);
        }
        RAM.remove(id);
    }

    @Override
    public void reloadWorks(int id) throws DatabaseException {
        WorkRecordBook recordBook = WorkRecordBook.getInstance(id);
        User user = RAM.get(id).user;
        Account account = new Account(user, recordBook);
        RAM.put(id, account);
    }

    @Override
    public void ensureLoggingIn(int id) throws DatabaseException {
        if (RAM.get(id) == null) {
            User user = userDAO.get(id);
            put(user);
        }
    }


    public static class Account extends Repository.Account {

        private final User user;
        private final  WorkRecordBook recordBook;

        private Account(User user, WorkRecordBook recordBook) {
            super(user.getId());
            this.user = user;
            this.recordBook = recordBook;
        }

        public User user() {
            return user;
        }

        public WorkRecordBook recordBook() {
            return recordBook;
        }
    }
}