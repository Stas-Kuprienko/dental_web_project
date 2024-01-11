package edu.dental.service;

import edu.dental.WebAPI;
import edu.dental.domain.records.WorkRecordBook;
import edu.dental.dto.DentalWorkDto;
import edu.dental.dto.ProductMapDto;
import edu.dental.entities.User;
import edu.dental.service.monitor.Monitorable;

import java.util.List;

public interface Repository {

    String paramUser = "user";

    static Repository getInstance() {
        return WebAPI.INSTANCE.getRepository();
    }

    void startMonitor();

    void updateAccountLastAction(int userId);

    Repository.Account put(User user, WorkRecordBook recordBook);

    Account get(int id);

    boolean put(User user);

    List<DentalWorkDto> getDentalWorkDtoList(int id);

    ProductMapDto getProductMapDto(int id);

    User getUser(int id);

    WorkRecordBook getRecordBook(int id);

    void delete(int id);


    abstract class Account extends Monitorable {

        protected Account(int key) {
            super(key);
        }

        abstract public User user();
        abstract public WorkRecordBook recordBook();
    }
}
