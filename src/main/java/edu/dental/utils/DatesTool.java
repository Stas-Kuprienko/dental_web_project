package edu.dental.utils;

import edu.dental.domain.entities.WorkRecord;

import java.time.LocalDate;
import java.time.Month;

public class DatesTool {

    public static LocalDate toLocalDate(String date) {
        if (date == null || date.isEmpty()) {
            throw new NullPointerException("The given date is null or empty.");
        }
        String[] strings = date.split("-");
        if (strings.length < 2) {
            throw new IllegalArgumentException("The given date is incorrect.");
        }
        int day = Integer.parseInt(strings[0]);
        int month = Integer.parseInt(strings[1]);
        int year = 0;
        if (strings.length >= 3) {
            year = Integer.parseInt(strings[2]);
        } else {
            year = LocalDate.now().getYear();
        }
        return LocalDate.of(year, month, day);
    }

    public static boolean isCurrentMonth(LocalDate date, int splitDay) {
        LocalDate today = LocalDate.now();
        Month month;
        if (today.getDayOfMonth() < splitDay) {
            month = today.getMonth().minus(1);
        } else {
            month = today.getMonth();
        }
        return date.getMonth().getValue() <= month.getValue();
    }
}
