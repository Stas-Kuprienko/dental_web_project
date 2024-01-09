package edu.dental.service;

import edu.dental.beans.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public enum WebRepository {

    INSTANCE;

    WebRepository() {
        this.RAM = new ConcurrentHashMap<>();
    }

    private final ConcurrentHashMap<Integer, Account> RAM;


    public void startMonitor() {
        WebLifecycleMonitor.INSTANCE.revision(RAM);
    }

    public void updateAccountLastAction(int userId) {
        RAM.get(userId).updateLastAction();
    }

    public String getToken(int id) {
        return RAM.get(id).user.jwt();
    }

    public UserDto getUser(int id) {
        return RAM.get(id).user;
    }

    public void updateUser(UserDto user) {
        RAM.get(user.id()).setUser(user);
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

    public boolean isExist(int userId) {
        Account account = RAM.get(userId);
        return (account != null && account.user.id() == userId);
    }

    public void delete(int id) {
        RAM.remove(id);
    }

    public SalaryRecord getSalaryRecord(int userId) {
        int amount = RAM.get(userId).countSalary();
        LocalDate now = LocalDate.now();
        String month = now.getMonth().toString().toLowerCase();
        return new SalaryRecord(now.getYear(), month, amount);
    }


    public static class Account extends Monitorable {

        private UserDto user;
        private final ProductMap productMap;
        private List<DentalWork> dentalWorks;

        public Account(UserDto user, ProductMap productMap, List<DentalWork> dentalWorks) {
            super(user.id());
            this.user = user;
            this.productMap = productMap;
            this.dentalWorks = new ArrayList<>(dentalWorks);
        }

        private void setUser(UserDto user) {
            this.user = user;
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

        private int countSalary() {
            int amount = 0;
            for (DentalWork dw : dentalWorks) {
                for (Product p : dw.products()) {
                    amount += p.price() * p.quantity();
                }
            }
            return amount;
        }
    }
}
