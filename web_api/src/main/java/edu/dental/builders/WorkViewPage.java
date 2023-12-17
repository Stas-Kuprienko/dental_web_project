package edu.dental.builders;

import edu.dental.dto.Product;
import edu.dental.dto.ProductMap;
import edu.dental.dto.DentalWork;
import edu.dental.web.Repository;
import jakarta.servlet.http.HttpServletRequest;

import java.util.Arrays;
import java.util.Iterator;

import static edu.dental.builders.HtmlTag.OPTION;

public class WorkViewPage {

    public final OptionBuilder option;

    public final DentalWork work;
    private final Iterator<Product> products;

    public WorkViewPage(HttpServletRequest request) {
        String user = (String) request.getSession(false).getAttribute("user");
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
            String login = (String) request.getSession().getAttribute("user");
            ProductMap productMap = Repository.getInstance().getMapDto(login);
            if (productMap == null || productMap.isEmpty()) {
                this.map = Arrays.stream(new String[] {" "}).iterator();
            } else {
                this.map = Arrays.stream(productMap.getKeys()).iterator();
            }
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
