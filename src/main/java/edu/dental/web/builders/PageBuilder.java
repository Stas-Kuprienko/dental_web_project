package edu.dental.web.builders;

import edu.dental.domain.APIManager;
import edu.dental.domain.DatesTool;
import edu.dental.domain.entities.IDentalWork;
import edu.dental.domain.entities.Product;
import edu.dental.domain.records.ProductMap;
import edu.dental.domain.records.WorkRecordBook;
import jakarta.servlet.http.HttpServletRequest;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import static edu.dental.web.builders.HtmlTag.*;

public final class PageBuilder {

    private static final PageBuilder instance;
    static {
        instance = new PageBuilder();
    }
    private PageBuilder() {}

    public static synchronized PageBuilder get() {
        return instance;
    }


    public static StringBuilder line(StringBuilder str, TAG2 tag, String value) {
        str.append(tag.o).append(value).append(tag.c).append("\n\t\t");
        return str;
    }

    public static String month() {
        String[] yearNMonth = DatesTool.getYearAndMonth();
        return yearNMonth[1].toUpperCase() + " - " + yearNMonth[0];
    }

    public static class Header {

        private final String[] map;
        private int cursor;

        public Header(HttpServletRequest request) {
            String login = (String) request.getSession().getAttribute("user");
            ProductMap productMap = APIManager.INSTANCE.getRepository().getRecordBook(login).getMap();
            if (productMap == null || productMap.isEmpty()) {
                this.map = new String[] {" "};
            } else {
                this.map = productMap.keysToArray();
            }
            this.cursor = 0;
        }

        public boolean hasNext() {
            return cursor < map.length;
        }

        public String next() {
            String s = map[cursor];
            cursor += 1;
            return s;
        }
    }

    public static class RowBuilder {

        private final Iterator<IDentalWork> works;
        private final String[] map;
        private int cursor;

        public RowBuilder(HttpServletRequest request) {
            String login = (String) request.getSession().getAttribute("user");
            WorkRecordBook recordBook = APIManager.INSTANCE.getRepository().getRecordBook(login);
            List<IDentalWork> list = recordBook.getList();
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
            IDentalWork dw = works.next();
            StringBuilder str = new StringBuilder();
            TAG2 tagA = dw.getStatus().equals(IDentalWork.Status.MAKE) ? TAG2.A_TR
                    : dw.getStatus().equals(IDentalWork.Status.CLOSED) ? TAG2.A_TR_CLOSED
                    : TAG2.A_TR_PAID;
            str.append(String.format(tagA.o, dw.getId())).append("\n\t\t");
            line(str, TAG2.DIV_TD, dw.getPatient());
            line(str, TAG2.DIV_TD, dw.getClinic());
            if (dw.getProducts().isEmpty()) {
                for (String ignored : map) {
                    line(str, TAG2.DIV_TD, " ");
                }
            } else {
                for (String s : map) {
                    Product p = dw.findProduct(s);
                    line(str, TAG2.DIV_TD, p == null ? " " : String.valueOf(p.quantity()));
                }
            }
            line(str, TAG2.DIV_TD, String.valueOf(dw.getComplete()));
            line(str, TAG2.DIV_TD, String.valueOf(dw.getAccepted()));
            str.append(tagA.c);
            return str.toString();
        }
    }




    public String build(ProductMap map, Collection<IDentalWork> works) {
        String thread = buildThread(map);
        String tBody = buildTBody(map, works);
        StringBuilder content = new StringBuilder();
        String[] yearAndMonth = DatesTool.getYearAndMonth();
        String month = yearAndMonth[1].toUpperCase() + " - " + yearAndMonth[0];
        content.append(String.format(TABLE_MONTH.tag, month)).append("\n\t")
                .append(TABLE_STATUS.tag).append("\n\t").append(TAG2.DIV_TABLE.o)
                .append("\n\t").append(thread).append(tBody).append(TAG2.DIV_TABLE.c);
        return String.format(BASIC_APP.tag, content);
    }

    private String buildThread(ProductMap map) {
        StringBuilder products = new StringBuilder();
        for (String s : map.keysToArray()) {
            line(products, TAG2.DIV_TH, s.toUpperCase());
        }
        return String.format(TABLE_THEAD.tag, products);
    }

    private String buildTBody(ProductMap map, Collection<IDentalWork> works) {
        StringBuilder rows = new StringBuilder();
        rows.append(TAG2.DIV_TBODY.o).append("\n\t");
        for (IDentalWork dw : works) {
            if (dw.getStatus().name().equals("MAKE")) {
                rows.append(String.format(TAG2.A_TR.o, dw.getId()));
            } else if (dw.getStatus().name().equals("CLOSED")) {
                rows.append(String.format(TAG2.A_TR_CLOSED.o, dw.getId()));
            } else if (dw.getStatus().name().equals("PAID")) {
                rows.append(String.format(TAG2.A_TR_PAID.o, dw.getId()));
            }
            buildRow(rows.append("\n\t"), map, dw);
            rows.append(TAG2.A_TR.c).append("\n\t");
        }
        return rows.append(TAG2.DIV_TBODY.c).toString();
    }

    private void buildRow(StringBuilder str, ProductMap map, IDentalWork dw) {
        line(str, TAG2.DIV_TD, dw.getPatient());
        line(str, TAG2.DIV_TD, dw.getClinic());
        if (dw.getProducts().isEmpty()) {
            for (String ignored : map.keysToArray()) {
                line(str, TAG2.DIV_TD, " ");
            }
        } else {
            for (String s : map.keysToArray()) {
                Product p = dw.findProduct(s);
                line(str, TAG2.DIV_TD, p == null ? " " : String.valueOf(p.quantity()));
            }
        }
        line(str, TAG2.DIV_TD, String.valueOf(dw.getComplete()));
    }
}
