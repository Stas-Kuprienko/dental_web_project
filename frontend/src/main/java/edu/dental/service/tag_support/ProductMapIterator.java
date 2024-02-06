package edu.dental.service.tag_support;

import edu.dental.beans.DentalWork;
import edu.dental.beans.Product;
import edu.dental.beans.ProductMap;
import jakarta.servlet.jsp.JspException;
import jakarta.servlet.jsp.JspWriter;
import jakarta.servlet.jsp.tagext.SimpleTagSupport;

import java.io.IOException;

public class ProductMapIterator extends SimpleTagSupport {

    private String tag;
    private String style;
    private ProductMap.Item[] map;
    private DentalWork work;


    @Override
    public void doTag() throws JspException, IOException {
        JspWriter out = getJspContext().getOut();
        String open = String.format("""
                <%s class="%s">""", tag, style);
        String close = "</" + tag + ">\n";
        String line;
        if (work.getProducts().length == 0) {
            for (ProductMap.Item ignored : map) {
                line = open + " " + close;
                out.print(line);
            }
        } else {
            for (ProductMap.Item item : map) {
                String quantity = " ";
                for (Product p : work.getProducts()) {
                    if (p.getTitle().equals(item.getKey())) {
                        quantity = String.valueOf(p.getQuantity());
                    }
                }
                line = open + quantity + close;
                out.print(line);
            }
        }
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public void setStyle(String style) {
        this.style = style;
    }

    public void setMap(ProductMap.Item[] map) {
        this.map = map;
    }

    public void setWork(DentalWork work) {
        this.work = work;
    }
}
