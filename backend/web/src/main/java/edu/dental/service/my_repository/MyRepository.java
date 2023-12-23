package edu.dental.service.my_repository;

import edu.dental.domain.records.WorkRecordBook;
import edu.dental.dto.DentalWork;
import edu.dental.entities.User;
import edu.dental.service.Repository;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public final class MyRepository implements Repository {

    private MyRepository() {
        RAM = new ConcurrentHashMap<>();
    }

    private final ConcurrentHashMap<Integer, Account> RAM;


    public Account put(User user, WorkRecordBook recordBook) {
        Account account = new Account(user, recordBook);
        RAM.put(user.getId(), account);
        return account;
    }

    @Override
    public List<DentalWork> getDentalWorkDtoList(int id) {
        return get(id).recordBook.getRecords().stream().map(DentalWork::new).toList();
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


    public record Account(User user, WorkRecordBook recordBook) implements Repository.Account {}
}
