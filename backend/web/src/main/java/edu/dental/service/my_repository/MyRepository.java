package edu.dental.service.my_repository;

import edu.dental.WebException;
import edu.dental.database.DatabaseException;
import edu.dental.database.DatabaseService;
import edu.dental.database.dao.UserDAO;
import edu.dental.domain.records.WorkRecordBook;
import edu.dental.domain.records.WorkRecordBookException;
import edu.dental.security.SecurityException;
import edu.dental.service.AccountException;
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
    public User createNew(String name, String login, byte[] password) throws AccountException {
        User user = new User(name, login, password);
        try {
            userDAO.put(user);
            WorkRecordBook recordBook = WorkRecordBook.createNew(user.getId());
            Account account = new Account(user, recordBook);
            RAM.put(user.getId(), account);
            return user;
        } catch (DatabaseException e) {
            RAM.remove(user.getId());
            throw new AccountException(e);
        }
    }

    @Override
    public boolean put(User user) {
        try {
            WorkRecordBook recordBook = WorkRecordBook.getInstance(user.getId());
            Account account = new Account(user, recordBook);
            RAM.put(user.getId(), account);
            return true;
        } catch (WorkRecordBookException e) {
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
    public UserDto update(int userId, String field, String value) throws WebException {
        User user = getUser(userId);
        if (!updateFieldValue(field, value, user)) {
            return null;
        }
        return new UserDto(user);
    }

    @Override
    public void delete(int id) {
        RAM.remove(id);
    }

    @Override
    public void delete(int id, boolean fromDatabase) throws AccountException {
        if (fromDatabase) {
            try {
                userDAO.delete(id);
            } catch (DatabaseException e) {
                throw new AccountException(e);
            }
        }
        RAM.remove(id);
    }

    @Override
    public void reloadWorks(int id) throws AccountException {
        try {
            WorkRecordBook recordBook = WorkRecordBook.getInstance(id);
            User user = RAM.get(id).user;
            Account account = new Account(user, recordBook);
            RAM.put(id, account);
        } catch (WorkRecordBookException e) {
            throw new AccountException(e);
        }
    }

    @Override
    public void ensureLoggingIn(int id) throws WebException, AccountException {
        if (RAM.get(id) == null) {
            try {
                User user = userDAO.get(id);
                put(user);
            } catch (DatabaseException e) {
                if (e.getMessage().equals("The such object is not found.")) {
                    throw new WebException(WebException.CODE.NOT_FOUND);
                } throw new AccountException(e);
            }
        }
    }

    private boolean updateFieldValue(String field, String value, User user) throws WebException {
        switch (field) {
            case "name" -> {
                String oldName = user.getName();
                user.setName(value);
                try {
                    return userDAO.update(user);
                } catch (DatabaseException e) {
                    user.setName(oldName);
                    throw new WebException(WebException.CODE.SERVER_ERROR);
                }
            }
            case "email" -> {
                String oldEmail = user.getEmail();
                user.setEmail(value);
                try {
                    return userDAO.update(user);
                } catch (DatabaseException e) {
                    user.setEmail(oldEmail);
                    throw new WebException(WebException.CODE.SERVER_ERROR);
                }
            }
            case "password" -> {
                try {
                    return AuthenticationService.getInstance().updatePassword(user, value);
                } catch (SecurityException e) {
                    throw new WebException(WebException.CODE.SERVER_ERROR);
                }
            }
            default -> {
                return false;
            }
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