package edu.dental.service;

import edu.dental.beans.DentalWork;
import edu.dental.beans.ProductMap;
import edu.dental.beans.UserDto;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public enum WebRepository {

    INSTANCE;

    WebRepository() {
        this.RAM = new ConcurrentHashMap<>();
    }

    private final ConcurrentHashMap<Integer, Account> RAM;

    public String getToken(int id) {
        return RAM.get(id).user.jwt();
    }

    public ProductMap getMap(int id) {
        return RAM.get(id).productMap;
    }

    public List<DentalWork> getWorks(int id) {
        return RAM.get(id).dentalWorks;
    }

    public void setWorks(int id, List<DentalWork> list) {
        RAM.get(id).setDentalWorks(list);
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

    public void updateDentalWorkList(int userId, DentalWork dw) {
        Account account = RAM.get(userId);
        account.updateWorks(dw);
    }

    public void deleteDentalWork(int userId, int id) {
        Account account = RAM.get(userId);
        account.deleteWork(id);
    }

    public void delete(int id) {
        RAM.remove(id);
    }


    public static class Account {

        private final UserDto user;
        private final ProductMap productMap;
        private List<DentalWork> dentalWorks;

        public Account(UserDto user, ProductMap productMap, List<DentalWork> dentalWorks) {
            this.user = user;
            this.productMap = productMap;
            this.dentalWorks = new ArrayList<>(dentalWorks);
        }

        private void setDentalWorks(List<DentalWork> dentalWorks) {
            this.dentalWorks = new ArrayList<>(dentalWorks);
        }

        private void updateWorks(DentalWork dentalWork) {
            dentalWorks.stream().filter(dw -> dw.id() == dentalWork.id()).findAny().ifPresent(dentalWorks::remove);
            dentalWorks.add(dentalWork);
        }

        private void deleteWork(int id) {
            dentalWorks.stream().filter(dw -> dw.id() == id).findAny().ifPresent(dentalWorks::remove);
        }
    }
}
