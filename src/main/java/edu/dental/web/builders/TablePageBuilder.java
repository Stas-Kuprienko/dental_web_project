package edu.dental.web.builders;

import edu.dental.domain.entities.I_DentalWork;
import edu.dental.domain.entities.Product;
import edu.dental.domain.records.ProductMap;
import edu.dental.domain.records.WorkRecordBook;
import edu.dental.utils.DatesTool;

import java.util.Collection;
import java.util.NoSuchElementException;

import static edu.dental.web.builders.HtmlSamples.*;

public final class TablePageBuilder {

    private static final TablePageBuilder instance;
    static {
        instance = new TablePageBuilder();
    }
    private TablePageBuilder() {}

    public static synchronized TablePageBuilder get() {
        return instance;
    }



    public String build(ProductMap map, Collection<I_DentalWork> works) {
        String thread = buildThread(map);
        String tBody = buildTBody(map, works);
        StringBuilder content = new StringBuilder();
        String[] yearAndMonth = DatesTool.getYearAndMonth(WorkRecordBook.PAY_DAY);
        String month = yearAndMonth[1].toUpperCase() + " - " + yearAndMonth[0];
        content.append(String.format(TABLE_MONTH.tag, month)).append("\n\t")
                .append(TABLE_STATUS.tag).append("\n\t").append(tag2.DIV_TABLE.o)
                .append("\n\t").append(thread).append(tBody).append(tag2.DIV_TABLE.c);
        return String.format(BASIC.tag, content);
    }

    private String buildThread(ProductMap map) {
        StringBuilder products = new StringBuilder();
        for (String s : map.keysToArray()) {
            line(products, tag2.DIV_TH, s.toUpperCase());
        }
        return String.format(TABLE_THREAD.tag, products);
    }

    private String buildTBody(ProductMap map, Collection<I_DentalWork> works) {
        StringBuilder rows = new StringBuilder();
        rows.append(tag2.DIV_TBODY.o).append("\n\t");
        for (I_DentalWork dw : works) {
            if (dw.getStatus().name().equals("MAKE")) {
                rows.append(String.format(tag2.A_TR.o, dw.getId()));
            } else if (dw.getStatus().name().equals("CLOSED")) {
                rows.append(String.format(tag2.A_TR_CLOSED.o, dw.getId()));
            } else if (dw.getStatus().name().equals("PAID")) {
                rows.append(String.format(tag2.A_TR_PAID.o, dw.getId()));
            }
            buildRow(rows.append("\n\t"), map, dw);
            rows.append(tag2.A_TR.c).append("\n\t");
        }
        return rows.append(tag2.DIV_TBODY.c).toString();
    }

    private void buildRow(StringBuilder str, ProductMap map, I_DentalWork dw) {
        line(str, tag2.DIV_TD, dw.getPatient());
        line(str, tag2.DIV_TD, dw.getClinic());
        if (dw.getProducts().isEmpty()) {
            for (String ignored : map.keysToArray()) {
                line(str, tag2.DIV_TD, " ");
            }
        } else {
            for (String s : map.keysToArray()) {
                try {
                    Product p = dw.findProduct(s);
                    line(str, tag2.DIV_TD, String.valueOf(p.quantity()));
                } catch (NoSuchElementException | NullPointerException ignored) {
                    line(str, tag2.DIV_TD, " ");
                }
            }
        }
        line(str, tag2.DIV_TD, String.valueOf(dw.getComplete()));
    }
}
