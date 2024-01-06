package edu.dental.jsp_printers;

import edu.dental.beans.DentalWork;
import edu.dental.beans.Product;
import jakarta.servlet.http.HttpServletRequest;

import java.time.LocalDate;
import java.time.Month;
import java.util.Arrays;
import java.util.Iterator;
import java.util.NoSuchElementException;

import static edu.dental.jsp_printers.HtmlTag.*;

@SuppressWarnings("unused")
public class WorkListTable {

    private static final String href = "dental-work?id=";

    public final Header tableHead;

    private final DatesTool datesTool;
    private final String[] map;
    private final Iterator<DentalWork> iterator;

    public WorkListTable(HttpServletRequest request) {
        String year_month = (String) request.getAttribute("year-month");
        if (year_month == null || year_month.isEmpty()) {
            this.datesTool = new DatesTool();
        } else {
            String[] year_month_split = year_month.split("-");
            this.datesTool = new DatesTool(year_month_split[0], year_month_split[1]);
        }
        this.tableHead = new Header(request);
        this.map = (String[]) request.getAttribute("map");
        DentalWork[] works = (DentalWork[]) request.getAttribute("works");
        this.iterator = Arrays.stream(works).iterator();
        request.removeAttribute("year-month");
    }


    public String month() {
        return datesTool.getMonthToString() + " - " + datesTool.getYear();
    }

    public boolean hasNext() {
        return iterator.hasNext();
    }

    public String next() {
        DentalWork dw = iterator.next();
        StringBuilder str = new StringBuilder();
        HtmlTag tagA = dw.status().equals(DentalWork.Status.MAKE.toString()) ? A_TR
                : dw.status().equals(DentalWork.Status.CLOSED.toString()) ? A_TR_CLOSED
                : A_TR_PAID;
        str.append(String.format(tagA.o, href + dw.id())).append("\n\t\t");
        DIV_TD.line(str, dw.patient());
        DIV_TD.line(str, dw.clinic());
        if (dw.products().length == 0) {
            for (String ignored : map) {
                DIV_TD.line(str, "");
            }
        } else {
            for (String s : map) {
                Product p = findProduct(dw, s);
                DIV_TD.line(str, p == null ? " " : String.valueOf(p.quantity()));
            }
        }
        DIV_TD.line(str, dw.complete() != null ? String.valueOf(dw.complete()) : "");
        DIV_TD.line(str, String.valueOf(dw.accepted()));
        str.append(tagA.c);
        return str.toString();
    }

    public String input_for_sorting_current_month() {
        return String.format(WORK_VIEW.INPUT_FOR_SORTING.sample, datesTool.getCurrentYear(), datesTool.getCurrentMonth());
    }

    public String input_for_sorting_previous_month() {
        return String.format(WORK_VIEW.INPUT_FOR_SORTING.sample, datesTool.getYearOfPreviousMonth(), datesTool.getPreviousMonth());
    }

    public String form_get_works_by_month() {
        int month = datesTool.getCurrentMonth();
        String monthStr = month < 10 ? "0" + month : "" + month;
        return String.format(WORK_VIEW.FORM_FOR_MONTH.sample, datesTool.getYear(), monthStr);
    }

    public String hidden_input_year_and_month() {
        return String.format(WORK_VIEW.INPUT_FOR_SORTING.sample, datesTool.getYear(), datesTool.getMonth());
    }

    private Product findProduct(DentalWork dw, String type) {
        if (dw.products().length == 0) {
            throw new NoSuchElementException("the given DentalWork(id=" + dw.id() + ") doesn't has products.");
        }
        type = type.toLowerCase();
        for (Product p : dw.products()) {
            if (p.title().equals(type)) {
                return p;
            }
        }
        return null;
    }


    public static class Header {

        private final Iterator<String> map;

        private Header(HttpServletRequest request) {
            String[] map = (String[]) request.getAttribute("map");
            if (map == null || map.length == 0) {
                this.map = Arrays.stream(new String[] {" "}).iterator();
            } else {
                this.map = Arrays.stream(map).iterator();
            }
        }

        public boolean hasNext() {
            return map.hasNext();
        }

        public String next() {
            return map.next().toUpperCase();
        }
    }

    @SuppressWarnings("all")
    private final class DatesTool {

        private final LocalDate now;

        private DatesTool() {
            this.now = LocalDate.now();
            this.month = now.getMonth();
            this.year = now.getYear();
        }

        private DatesTool(String year, String month) {
            this.now = LocalDate.now();
            this.month = Month.of(Integer.parseInt(month));
            this.year = Integer.parseInt(year);
        }

        private final Month month;
        private final int year;

        private String getMonthToString() {
            return month.toString();
        }

        private String getCurrentMonthToString() {
            return now.getMonth().toString();
        }

        private int getMonth() {
            return month.getValue();
        }

        private int getCurrentMonth() {
            return now.getMonthValue();
        }

        private int getPreviousMonth() {
            return now.minusMonths(1).getMonthValue();
        }

        private int getYearOfPreviousMonth() {
            return now.minusMonths(1).getYear();
        }

        private int getYear() {
            return year;
        }

        private int getCurrentYear() {
            return now.getYear();
        }
    }
}
