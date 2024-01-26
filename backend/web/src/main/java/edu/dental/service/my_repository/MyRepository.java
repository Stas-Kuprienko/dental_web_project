package edu.dental.service.my_repository;

import edu.dental.domain.records.WorkRecordBook;
import edu.dental.domain.records.WorkRecordBookException;
import edu.dental.dto.DentalWorkDto;
import edu.dental.dto.ProductMapDto;
import edu.dental.entities.User;
import edu.dental.service.lifecycle.LifecycleMonitor;
import edu.dental.service.Repository;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public final class MyRepository implements Repository {

    private MyRepository() {
        RAM = new ConcurrentHashMap<>();
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

    public void putNew(User user) {
        WorkRecordBook recordBook = WorkRecordBook.createNew(user.getId());
        Account account = new Account(user, recordBook);
        RAM.put(user.getId(), account);
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
        return get(id).recordBook.getRecords().stream().map(DentalWorkDto::new).toList();
    }

    @Override
    public ProductMapDto getProductMapDto(int id) {
        return new ProductMapDto(get(id).recordBook.getProductMap());
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
    public void delete(int id) {
        RAM.remove(id);
    }

    @Override
    public Account get(int id) {
        return RAM.get(id);
    }

    @Override
    public void reloadWorks(int id) {
        try {
            WorkRecordBook recordBook = WorkRecordBook.getInstance(id);
            User user = RAM.get(id).user;
            Account account = new Account(user, recordBook);
            RAM.put(id, account);
        } catch (WorkRecordBookException e) {
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