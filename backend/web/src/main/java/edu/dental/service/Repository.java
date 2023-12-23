package edu.dental.service;

import edu.dental.WebAPI;
import edu.dental.domain.records.WorkRecordBook;
import edu.dental.dto.DentalWork;
import edu.dental.entities.User;
import edu.dental.service.my_repository.MyRepository;

import java.util.List;

public interface Repository {

    static Repository getInstance() {
        return WebAPI.INSTANCE.getRepository();
    }

    MyRepository.Account put(User user, WorkRecordBook recordBook);

    Account get(int id);

    List<DentalWork> getDentalWorkDtoList(int id);

    User getUser(int id);

    WorkRecordBook getRecordBook(int id);

    void delete(int id);


    interface Account {
        User user();
        WorkRecordBook recordBook();
    }
}
