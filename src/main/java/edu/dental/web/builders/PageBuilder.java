package edu.dental.web.builders;

import edu.dental.domain.entities.Product;
import edu.dental.domain.entities.dto.DentalWorkDTO;
import edu.dental.domain.records.ProductMap;
import edu.dental.web.Repository;
import jakarta.servlet.http.HttpServletRequest;

import java.util.Arrays;
import java.util.Iterator;

import static edu.dental.web.builders.HtmlTag.OPTION;

public final class PageBuilder {

    private PageBuilder() {}

    public static class OptionBuilder {

        private final Iterator<String> map;
        private final StringBuilder str;

        public OptionBuilder(HttpServletRequest request) {
            String login = (String) request.getSession().getAttribute("user");
            ProductMap productMap = Repository.getInstance().getRecordBook(login).getMap();
            if (productMap == null || productMap.isEmpty()) {
                this.map = Arrays.stream(new String[] {" "}).iterator();
            } else {
                this.map = Arrays.stream(productMap.keysToArray()).iterator();
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

    public static class ProductListBuilder {

        private final Iterator<Product> iterator;

        public ProductListBuilder(HttpServletRequest request) {
            DentalWorkDTO work = (DentalWorkDTO) request.getAttribute("work");
            this.iterator = Arrays.stream(work.products()).iterator();
        }

        public boolean hasNext() {
            return iterator.hasNext();
        }

        public Product next() {
            return iterator.next();
        }
    }
}
