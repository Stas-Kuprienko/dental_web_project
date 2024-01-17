package edu.dental.jsp_printers;

import edu.dental.WebUtility;
import edu.dental.beans.DentalWork;
import edu.dental.beans.Product;
import edu.dental.beans.ProductMap;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import java.util.Arrays;
import java.util.Iterator;

import static edu.dental.jsp_printers.HtmlTag.OPTION;

@SuppressWarnings("unused")
public class WorkViewPage {

    public final OptionBuilder option;

    public final DentalWork work;
    private final Iterator<Product> products;

    public WorkViewPage(HttpServletRequest request) {
        this.work = (DentalWork) request.getAttribute("work");
        this.products = Arrays.stream(work.getProducts()).iterator();
        ProductMap map = (ProductMap) request.getSession().getAttribute(WebUtility.INSTANCE.sessionMap);
        String[] keysMap = map.getKeys();
        this.option = new OptionBuilder(keysMap);
    }

    public String inputId() {
        return String.format(HtmlTag.WORK_VIEW.INPUT_ID.sample, work.getId());
    }

    public String buttonId() {
        return String.format(HtmlTag.WORK_VIEW.BUTTON_ID.sample, work.getId());
    }

    public boolean hasNextProduct() {
        return products.hasNext();
    }

    public Product nextProduct() {
        Product product = products.next();
//        String DIV = product.getTitle() + " - " + product.getQuantity();
//        return String.format(HtmlTag.WORK_VIEW.PRODUCT_LIST.sample, DIV, work.getId(), product.getTitle());
        return product;
    }

    public static class OptionBuilder {

        private final Iterator<String> map;
        private final StringBuilder str;

        public OptionBuilder(HttpServletRequest request) {
            HttpSession session = request.getSession();
            ProductMap productMap = (ProductMap) session.getAttribute(WebUtility.INSTANCE.sessionMap);
            String[] map = productMap.getKeys();
            this.map = map != null ? Arrays.stream(map).iterator() :
                    Arrays.stream(new String[]{""}).iterator();
            str = new StringBuilder();
        }

        public OptionBuilder(String[] map) {
            this.map = map != null ? Arrays.stream(map).iterator() :
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
