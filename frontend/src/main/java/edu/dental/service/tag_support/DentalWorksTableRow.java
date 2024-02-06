package edu.dental.service.tag_support;

import edu.dental.beans.DentalWork;
import jakarta.servlet.jsp.JspException;
import jakarta.servlet.jsp.JspWriter;
import jakarta.servlet.jsp.tagext.SimpleTagSupport;

import java.io.IOException;
import java.io.StringWriter;

public class DentalWorksTableRow extends SimpleTagSupport {

    private String tag;
    private String href;
    private DentalWork work;


    @Override
    public void doTag() throws JspException, IOException {
        JspWriter out = getJspContext().getOut();
        StringWriter writer = new StringWriter();
        getJspBody().invoke(writer);
        CSS clas = defineStyle();
        out.print(String.format("""
                <%s class="%s" href="%s/%d">"""
                , tag, clas.css, href, work.getId()));
        out.print(writer.toString());
        out.print("</" + tag + ">\n");
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public void setHref(String href) {
        this.href = href;
    }

    public void setWork(DentalWork work) {
        this.work = work;
    }

    private CSS defineStyle() {
        String status = work.getStatus();
        if (status.equalsIgnoreCase("CLOSED")) {
            return CSS.tr_closed;
        } else if (status.equalsIgnoreCase("PAID")) {
            return CSS.tr_paid;
        } else {
            return CSS.tr;
        }
    }

    enum CSS {
        tr("tr"),
        tr_closed("tr-closed"),
        tr_paid("tr-paid");

        final String css;

        CSS(String css) {
            this.css = css;
        }
    }
}
