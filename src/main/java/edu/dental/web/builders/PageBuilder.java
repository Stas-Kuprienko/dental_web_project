package edu.dental.web.builders;

import edu.dental.domain.APIManager;
import edu.dental.domain.utils.DatesTool;
import edu.dental.domain.entities.DentalWork;
import edu.dental.domain.entities.Product;
import edu.dental.domain.records.ProductMap;
import edu.dental.domain.records.WorkRecordBook;
import edu.dental.web.Repository;
import jakarta.servlet.http.HttpServletRequest;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import static edu.dental.web.builders.HtmlTag.*;

public final class PageBuilder {

    private PageBuilder() {}


    public static String month() {
        String[] yearNMonth = DatesTool.getYearAndMonth();
        return yearNMonth[1].toUpperCase() + " - " + yearNMonth[0];
    }

    public static class Header {

        private final Iterator<String> map;

        public Header(HttpServletRequest request) {
            String login = (String) request.getSession().getAttribute("user");
            ProductMap productMap = APIManager.INSTANCE.getRepository().getRecordBook(login).getMap();
            if (productMap == null || productMap.isEmpty()) {
                this.map = Arrays.stream(new String[] {" "}).iterator();
            } else {
                this.map = Arrays.stream(productMap.keysToArray()).iterator();
            }
        }

        public boolean hasNext() {
            return map.hasNext();
        }

        public String next() {
            return map.next().toUpperCase();
        }
    }

    public static class RowBuilder {

        private final Iterator<DentalWork> works;
        private final String[] map;

        public RowBuilder(HttpServletRequest request) {
            String login = (String) request.getSession().getAttribute("user");
            WorkRecordBook recordBook = APIManager.INSTANCE.getRepository().getRecordBook(login);
            List<DentalWork> list = recordBook.getList();
            this.works = list.iterator();
            ProductMap productMap = APIManager.INSTANCE.getRepository().getRecordBook(login).getMap();
            if (productMap == null || productMap.isEmpty()) {
                this.map = new String[] {" "};
            } else {
                this.map = productMap.keysToArray();
            }
        }

        public boolean hasNext() {
            return works.hasNext();
        }

        public String next() {
            DentalWork dw = works.next();
            StringBuilder str = new StringBuilder();
            HtmlTag tagA = dw.getStatus().equals(DentalWork.Status.MAKE) ? A_TR
                    : dw.getStatus().equals(DentalWork.Status.CLOSED) ? A_TR_CLOSED
                    : A_TR_PAID;
            str.append(String.format(tagA.o, dw.getId())).append("\n\t\t");
            DIV_TD.line(str, dw.getPatient());
            DIV_TD.line(str, dw.getClinic());
            if (dw.getProducts().isEmpty()) {
                for (String ignored : map) {
                    DIV_TD.line(str, "");
                }
            } else {
                for (String s : map) {
                    Product p = WorkRecordBook.findProduct(dw, s);
                    DIV_TD.line(str, p == null ? " " : String.valueOf(p.quantity()));
                }
            }
            DIV_TD.line(str, String.valueOf(dw.getComplete()));
            DIV_TD.line(str, String.valueOf(dw.getAccepted()));
            str.append(tagA.c);
            return str.toString();
        }
    }

    public static class OptionBuilder {

        private final Iterator<String> map;
        private final StringBuilder str;

        public OptionBuilder(HttpServletRequest request) {
            String login = (String) request.getSession().getAttribute("user");
            ProductMap productMap = Repository.getInstance().getRecordBook(login).getMap();
            if (productMap == null || productMap.isEmpty()) {
                this.map = Arrays.stream(new String[] {" "}).iterator();
            } else {
                this.map = Arrays.stream(productMap.keysToArray()).iterator();
            }
            str = new StringBuilder();
        }

        public boolean hasNext() {
            return map.hasNext();
        }

        public String next() {
            str.setLength(0);
            String value = map.next();
            return String.format(OPTION.line(str, value).toString(), value);
        }
    }
}
