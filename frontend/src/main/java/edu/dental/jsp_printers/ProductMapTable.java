package edu.dental.jsp_printers;

import edu.dental.beans.ProductMap;
import edu.dental.service.Repository;
import jakarta.servlet.http.HttpServletRequest;


import java.util.Iterator;

import static edu.dental.jsp_printers.HtmlTag.*;

public class ProductMapTable {

    private static final String href = "product-map";

    private final Iterator<ProductMap.Item> iterator;
    private final int id;

    public ProductMapTable(HttpServletRequest request) {
        String user = (String) request.getSession().getAttribute("user");
        ProductMap map = Repository.INSTANCE.getMap(0);
        this.iterator = map.iterator();
        String strId = request.getParameter("id");
        id = (strId == null || strId.isEmpty()) ? 0 : Integer.parseInt(strId);
    }

    public boolean hasNext() {
        return iterator.hasNext();
    }

    public String next() {
        if (id > 0) {
            return next(id);
        }
        ProductMap.Item item = iterator.next();
        StringBuilder str = new StringBuilder();
        str.append(String.format(A_TR.o, href, item.id()));
        DIV_TD.line(str, item.key());
        DIV_TD.line(str, String.valueOf(item.value()));
        str.append(A_TR.c);
        return str.toString();
    }

    private String next(int id) {
        ProductMap.Item item = iterator.next();
        StringBuilder str = new StringBuilder();
        if (item.id() == id) {
            str.append(A_TR_WITHOUT_HREF.o);
            DIV_TD.line(str, item.key());
            str.append(String.format(HtmlTag.PRODUCT_VIEW.FORM.sample, item.key(), item.id(), item.key(), item.id()));
        } else {
            str.append(String.format(A_TR.o, href, item.id()));
            DIV_TD.line(str, item.key());
            DIV_TD.line(str, String.valueOf(item.value()));
        }
        str.append(A_TR.c);
        return str.toString();
    }
}