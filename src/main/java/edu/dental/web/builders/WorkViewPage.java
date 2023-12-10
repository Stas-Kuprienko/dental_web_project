package edu.dental.web.builders;

import edu.dental.domain.entities.Product;
import edu.dental.domain.entities.dto.DentalWorkDTO;
import jakarta.servlet.http.HttpServletRequest;

import java.util.Arrays;
import java.util.Iterator;

public class WorkViewPage {

    public final DentalWorkDTO work;
    private final Iterator<Product> products;

    public WorkViewPage(HttpServletRequest request) {
        String user = (String) request.getSession(false).getAttribute("user");
        this.work = (DentalWorkDTO) request.getAttribute("work");
        this.products = Arrays.stream(work.products()).iterator();
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
}
