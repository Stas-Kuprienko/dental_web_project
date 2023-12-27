package edu.dental.jsp_printers;

import edu.dental.beans.DentalWork;
import edu.dental.beans.Product;
import jakarta.servlet.http.HttpServletRequest;

import java.util.Arrays;
import java.util.Iterator;

import static edu.dental.jsp_printers.HtmlTag.OPTION;

public class WorkViewPage {

    public final OptionBuilder option;

    public final DentalWork work;
    private final Iterator<Product> products;

    public WorkViewPage(HttpServletRequest request) {
        this.work = (DentalWork) request.getAttribute("work");
        this.products = Arrays.stream(work.products()).iterator();
        this.option = new OptionBuilder(request);
    }

    public String inputId() {
        return String.format(HtmlTag.WORK_VIEW.INPUT_ID.sample, work.id());
    }

    public String buttonId() {
        return String.format(HtmlTag.WORK_VIEW.BUTTON_ID.sample, work.id());
    }

    public boolean hasNextProduct() {
        return products.hasNext();
    }

    public String nextProduct() {
        Product product = products.next();
        String DIV = product.title() + " - " + product.quantity();
        return String.format(HtmlTag.WORK_VIEW.PRODUCT_LIST.sample, DIV, work.id(), product.title());
    }

    public static class OptionBuilder {

        private final Iterator<String> map;
        private final StringBuilder str;

        public OptionBuilder(HttpServletRequest request) {
            String[] productMap = (String[]) request.getAttribute("map");
            this.map = productMap != null ? Arrays.stream(productMap).iterator() :
                                            Arrays.stream(new String[]{""}).iterator();
            str = new StringBuilder();
        }

        public boolean hasNext() {
            return map.hasNext();
        }

        public String next() {
            str.setLength(0);
            String value = map.next();
            return String.format(OPTION.line(str, value).toString(), value);
        }
    }
}
