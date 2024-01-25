package edu.dental.tag_support;

import jakarta.servlet.http.HttpServletRequest;

import java.time.LocalDate;
import java.time.Month;

@SuppressWarnings("unused")
public class HeaderMonth {

    private final Month month;
    private final String monthValue;
    private final String prevMonthValue;
    private final String nowMonthValue;
    private final int year;
    private final int prevYear;
    private final int nowYear;


    public HeaderMonth(HttpServletRequest request) {
        LocalDate now = LocalDate.now();
        String year_month = (String) request.getAttribute("year-month");
        if (year_month == null || year_month.isEmpty()) {
            this.year = now.getYear();
            this.month = now.getMonth();
        } else {
            String[] year_month_split = year_month.split("-");
            this.year = Integer.parseInt(year_month_split[0]);
            this.month = Month.of(Integer.parseInt(year_month_split[1]));
        }
        this.nowYear = now.getYear();
        this.prevYear = nowYear - 1;
        int monthInt = month.getValue();
        this.monthValue = monthInt < 10 ? "0" + monthInt : "" + monthInt;
        monthInt = now.getMonthValue();
        this.nowMonthValue = monthInt < 10 ? "0" + monthInt : "" + monthInt;
        monthInt = now.getMonth().minus(1).getValue();
        this.prevMonthValue = monthInt < 10 ? "0" + monthInt : "" + monthInt;
    }


    public String month() {
        return month + " - " + year;
    }

    public Month getMonth() {
        return month;
    }

    public String getMonthValue() {
        return monthValue;
    }

    public String getPrevMonthValue() {
        return prevMonthValue;
    }

    public String getNowMonthValue() {
        return nowMonthValue;
    }

    public int getYear() {
        return year;
    }

    public int getPrevYear() {
        return prevYear;
    }

    public int getNowYear() {
        return nowYear;
    }
}
