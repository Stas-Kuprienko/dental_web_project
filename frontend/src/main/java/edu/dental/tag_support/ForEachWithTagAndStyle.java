package edu.dental.tag_support;

import jakarta.servlet.jsp.JspException;
import jakarta.servlet.jsp.JspWriter;
import jakarta.servlet.jsp.tagext.SimpleTagSupport;

import java.io.IOException;

public class ForEachWithTagAndStyle extends SimpleTagSupport {

    private String tag;
    private String style;
    private String[] items;

    //TODO fixing
    // <d:forEach tag="div" style="th" items="${sessionScope.map.getKeys()}"/>

    @Override
    public void doTag() throws JspException, IOException {
        JspWriter out = getJspContext().getOut();
        String open = String.format("""
                <%s class="%s">""", tag, style);
        String close = "</" + tag + ">\n";
        String line;
        for (String s : items) {
            line = open + s.toUpperCase() + close;
            out.print(line);
        }
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public void setStyle(String style) {
        this.style = style;
    }

    public void setItems(String[] items) {
        this.items = items;
    }
}