package edu.dental.builders;

import edu.dental.beans.ProductMap;
import edu.dental.service.Repository;
import jakarta.servlet.http.HttpServletRequest;


import java.util.Iterator;

import static edu.dental.builders.HtmlTag.*;

public class ProductMapTable {

    private static final String href = "product-map";

    private final Iterator<ProductMap.Item> iterator;
    private final int id;

    public ProductMapTable(HttpServletRequest request) {
        String user = (String) request.getSession().getAttribute("user");
        ProductMap map = Repository.getInstance().getMap(user);
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
        str.append(String.format(A_TR.o, href, item.getId()));
        DIV_TD.line(str, item.getKey());
        DIV_TD.line(str, String.valueOf(item.getValue()));
        str.append(A_TR.c);
        return str.toString();
    }

    private String next(int id) {
        ProductMap.Item item = iterator.next();
        StringBuilder str = new StringBuilder();
        if (item.getId() == id) {
            str.append(A_TR_WITHOUT_HREF.o);
            DIV_TD.line(str, item.getKey());
            str.append(String.format(HtmlTag.PRODUCT_VIEW.FORM.sample, item.getKey(), item.getId(), item.getKey(), item.getId()));
        } else {
            str.append(String.format(A_TR.o, href, item.getId()));
            DIV_TD.line(str, item.getKey());
            DIV_TD.line(str, String.valueOf(item.getValue()));
        }
        str.append(A_TR.c);
        return str.toString();
    }
}