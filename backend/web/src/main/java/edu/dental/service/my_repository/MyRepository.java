package edu.dental.service.my_repository;

import edu.dental.domain.records.WorkRecordBook;
import edu.dental.domain.records.WorkRecordBookException;
import edu.dental.dto.DentalWorkDto;
import edu.dental.dto.ProductMapDto;
import edu.dental.entities.User;
import edu.dental.service.AbstractLifecycleMonitor;
import edu.dental.service.Repository;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

public final class MyRepository implements Repository {

    private static final int lifecycle = 1;
    private final AccountLifecycleMonitor monitor;

    private MyRepository() {
        RAM = new ConcurrentHashMap<>();
        monitor = new AccountLifecycleMonitor(lifecycle);
    }

    private final ConcurrentHashMap<Integer, Account> RAM;


    @Override
    public void startMonitor() {
        this.monitor.revision();
    }

    @Override
    public void updateAccountLastAction(int userId) {
        RAM.get(userId).updateTime();
    }

    public Account put(User user, WorkRecordBook recordBook) {
        Account account = new Account(user, recordBook);
        RAM.put(user.getId(), account);
        return account;
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
        return RAM.get(id).user;
    }

    @Override
    public WorkRecordBook getRecordBook(int id) {
        return RAM.get(id).recordBook;
    }

    @Override
    public void delete(int id) {
        RAM.remove(id);
    }

    @Override
    public Account get(int id) {
        return RAM.get(id);
    }


    public static class Account implements Repository.Account {

        private long time;
        private final User user;
        private final  WorkRecordBook recordBook;

        private Account(User user, WorkRecordBook recordBook) {
            this.user = user;
            this.recordBook = recordBook;
            this.time = System.currentTimeMillis();
        }

        @Override
        public long lastAction() {
            return time;
        }

        @Override
        public void updateTime() {
            this.time = System.currentTimeMillis();
        }

        @Override
        public User user() {
            return user;
        }

        @Override
        public WorkRecordBook recordBook() {
            return recordBook;
        }
    }


    private class AccountLifecycleMonitor extends AbstractLifecycleMonitor {

        public AccountLifecycleMonitor(int minute) {
            super(minute);
        }

        @Override
        public void revision() {
            Runnable runnable = this.new Commandant();
            scheduleService.schedule(runnable, lifecycle, TimeUnit.MILLISECONDS);
        }

        private class Commandant implements Runnable {

            @Override
            public void run() {
                for (Account a : RAM.values()) {
                    if ((System.currentTimeMillis() - a.lastAction()) > lifecycle) {
                        RAM.remove(a.user().getId());
                    }
                }
                revision();
            }
        }
    }
}
