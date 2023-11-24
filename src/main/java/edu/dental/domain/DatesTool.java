package edu.dental.domain;

import java.time.LocalDate;
import java.time.Month;

public final class DatesTool {
    private DatesTool() {}


    public static boolean isCurrentMonth(LocalDate date, int splitDay) {
        LocalDate today = LocalDate.now();
        Month month;
        if (today.getDayOfMonth() < splitDay) {
            month = today.getMonth().minus(1);
        } else {
            month = today.getMonth();
        }
        return date != null && date.getMonth().getValue() <= month.getValue();
    }

    public static String[] getYearAndMonth(int spitDay) {
        LocalDate today = LocalDate.now();
        Month month;
        int year;
        if (today.getDayOfMonth() > spitDay) {
            month = today.getMonth();
            year = today.getYear();
        } else {
            month = today.getMonth().minus(1);
            if (month == Month.DECEMBER) {
                year = today.getYear() - 1;
            } else {
                year = today.getYear();
            }
        }
        String[] result = new String[2];
        result[0] = String.valueOf(year);
        result[1] = month.toString().toLowerCase();
        return result;

    }

    public static String[][] buildMonthStringArray(int count) {
        int year = LocalDate.now().getYear();
        Month month = LocalDate.now().getMonth().minus(1);
        String[][] result = new String[count][2];
        for (int i = 0; i < count; i++) {
            String[] yearNMonth = new String[] {String.valueOf(year), month.toString()};
            result[i] = yearNMonth;
            month = month.plus(1);
            if (month.equals(Month.JANUARY)) {
                year += 1;
            }
        }
        return result;
    }
}
