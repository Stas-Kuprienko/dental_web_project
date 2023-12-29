package edu.dental.jsp_printers;

import edu.dental.WebAPI;
import edu.dental.beans.DentalWork;
import edu.dental.beans.Product;
import edu.dental.beans.ProductMap;
import edu.dental.service.WebRepository;
import jakarta.servlet.http.HttpServletRequest;

import java.util.Arrays;
import java.util.Iterator;
import java.util.NoSuchElementException;

import static edu.dental.jsp_printers.HtmlTag.*;

public class WorkListTable {

    private static final String href = "dental-work";

    public final Header tableHead;

    private final String[] map;
    private final Iterator<DentalWork> iterator;

    public WorkListTable(HttpServletRequest request) {
        this.tableHead = new Header(request);
        ProductMap map = (ProductMap) request.getAttribute("map");
        this.map = map.getKeys();
        DentalWork[] works = (DentalWork[]) request.getAttribute("works");
        this.iterator = Arrays.stream(works).iterator();
    }


    public static String month() {
        //TODO !!!!!!!
        return "";
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
        str.append(String.format(tagA.o, href, dw.id())).append("\n\t\t");
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
            int userId = (int) request.getSession().getAttribute(WebAPI.INSTANCE.sessionAttribute);
            ProductMap productMap = WebRepository.INSTANCE.getMap(userId);
            if (productMap == null || productMap.isEmpty()) {
                this.map = Arrays.stream(new String[] {" "}).iterator();
            } else {
                this.map = Arrays.stream(productMap.getKeys()).iterator();
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
