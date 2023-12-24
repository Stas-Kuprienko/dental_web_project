package edu.dental.service;

import edu.dental.beans.DentalWork;
import edu.dental.beans.ProductMap;
import edu.dental.beans.UserDto;

import jakarta.servlet.http.HttpServletRequest;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class Repository {

    private Repository() {
        this.RAM = new ConcurrentHashMap<>();
    }
    static {
        instance = new Repository();
    }

    private static final Repository instance;

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

    public void delete(int id) {
        RAM.remove(id);
    }

    public static Repository getInstance() {
        return instance;
    }

    public record Account(UserDto user, ProductMap map, List<DentalWork> works) {}
}
