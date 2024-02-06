package edu.dental.service.tag_support;

import edu.dental.beans.ProductMap;
import jakarta.servlet.jsp.JspException;
import jakarta.servlet.jsp.JspWriter;
import jakarta.servlet.jsp.tagext.SimpleTagSupport;

import java.io.IOException;

public class HeaderProductMap extends SimpleTagSupport {

    private String tag;
    private String style;
    private ProductMap.Item[] items;


    @Override
    public void doTag() throws JspException, IOException {
        JspWriter out = getJspContext().getOut();
        String open = String.format("""
                <%s class="%s">""", tag, style);
        String close = "</" + tag + ">\n";
        String line;
        for (ProductMap.Item item : items) {
            line = open + item.getKey().toUpperCase() + close;
            out.print(line);
        }
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public void setStyle(String style) {
        this.style = style;
    }

    public void setItems(ProductMap.Item[] items) {
        this.items = items;
    }
}