package edu.dental.service;

import edu.dental.beans.DentalWork;
import edu.dental.beans.ProductMap;
import edu.dental.beans.UserDto;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public enum Repository {

    INSTANCE;

    Repository() {
        this.RAM = new ConcurrentHashMap<>();
    }

    private final ConcurrentHashMap<Integer, Account> RAM;


    public ProductMap getMap(int id) {
        return RAM.get(id).map;
    }

    public List<DentalWork> getWorks(int id) {
        return RAM.get(id).works;
    }

    public void setAccount(UserDto user, List<DentalWork> works, ProductMap map) {
        Account account = new Account(user, map, works);
        RAM.put(user.id(), account);
    }

    public void addNew(UserDto userDto) {
        List<DentalWork> workList = new ArrayList<>();
        Account account = new Account(userDto, new ProductMap(), workList);
        RAM.put(userDto.id(), account);
    }

    public void delete(int id) {
        RAM.remove(id);
    }

    public record Account(UserDto user, ProductMap map, List<DentalWork> works) {}
}
