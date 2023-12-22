package edu.dental.service;

import edu.dental.beans.DentalWork;
import edu.dental.beans.ProductMap;
import jakarta.servlet.http.HttpServletRequest;

import java.util.Arrays;
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

    private final ConcurrentHashMap<String, Account> RAM;


    public ProductMap getMap(String login) {
        return RAM.get(login).map;
    }

    public List<DentalWork> getWorks(String login) {
        return RAM.get(login).works;
    }

    public void setAccount(String login, Account.DTO dto) {
        Account account = Account.create(dto);
        RAM.put(login, account);
    }

    public void setDtoAttributes(HttpServletRequest request, String user) {
        ProductMap map = getMap(user);
        DentalWork[] works = new DentalWork[0];
        getWorks(user).toArray(works);
        request.setAttribute("map", map);
        request.setAttribute("works", works);
    }

    public void delete(String login) {
        RAM.remove(login);
    }

    public static Repository getInstance() {
        return instance;
    }

    public record Account(ProductMap map, List<DentalWork> works) {

        public static Account create(DTO dto) {
            return new Account(dto.map, Arrays.asList(dto.works));
        }

        public static class DTO {
            private final ProductMap map;
            private final DentalWork[] works;

            private DTO(ProductMap map, DentalWork[] works) {
                this.map = map;
                this.works = works;
            }
        }
    }
}
