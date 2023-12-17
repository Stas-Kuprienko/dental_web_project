package edu.dental.builders;

import edu.dental.WebAPI;
import edu.dental.domain.entities.DentalWork;
import edu.dental.domain.entities.Product;
import edu.dental.domain.entities.dto.DentalWorkDTO;
import edu.dental.domain.entities.dto.ProductMapDTO;
import edu.dental.domain.records.ProductMap;
import edu.dental.domain.records.WorkRecordBook;
import edu.dental.domain.utils.DatesTool;
import jakarta.servlet.http.HttpServletRequest;

import java.util.Arrays;
import java.util.Iterator;

import static edu.dental.builders.HtmlTag.*;

public class WorkListTable {

    private static final String href = "dental-work";

    public final Header tableHead;

    private final String[] map;
    private final Iterator<DentalWorkDTO> iterator;

    public WorkListTable(HttpServletRequest request) {
        this.tableHead = new Header(request);
        ProductMapDTO map = (ProductMapDTO) request.getAttribute("map");
        this.map = map.getKeys();
        DentalWorkDTO[] works = (DentalWorkDTO[]) request.getAttribute("works");
        this.iterator = Arrays.stream(works).iterator();
    }


    public static String month() {
        String[] yearNMonth = DatesTool.getYearAndMonth();
        return yearNMonth[1].toUpperCase() + " - " + yearNMonth[0];
    }

    public boolean hasNext() {
        return iterator.hasNext();
    }

    public String next() {
        DentalWorkDTO dw = iterator.next();
        StringBuilder str = new StringBuilder();
        HtmlTag tagA = dw.status().equals(DentalWork.Status.MAKE.toString()) ? A_TR
                : dw.status().equals(DentalWork.Status.CLOSED.toString()) ? A_TR_CLOSED
                : A_TR_PAID;
        str.append(String.format(tagA.o, href, dw.id())).append("\n\t\t");
        DIV_TD.line(str, dw.patient());
        DIV_TD.line(str, dw.clinic());
        if (dw.products().length == 0) {
            for (String ignored : map) {
                DIV_TD.line(str, "");
            }
        } else {
            for (String s : map) {
                Product p = WorkRecordBook.findProduct(dw, s);
                DIV_TD.line(str, p == null ? " " : String.valueOf(p.quantity()));
            }
        }
        DIV_TD.line(str, dw.complete() != null ? String.valueOf(dw.complete()) : "");
        DIV_TD.line(str, String.valueOf(dw.accepted()));
        str.append(tagA.c);
        return str.toString();
    }


    public static class Header {

        private final Iterator<String> map;

        private Header(HttpServletRequest request) {
            String login = (String) request.getSession().getAttribute("user");
            ProductMap productMap = WebAPI.INSTANCE.getRepository().getRecordBook(login).getMap();
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
}
