package edu.dental.web.builders;

import edu.dental.domain.entities.DentalWork;
import edu.dental.domain.entities.Product;
import edu.dental.domain.entities.dto.DentalWorkDTO;
import edu.dental.domain.entities.dto.ProductMapDTO;
import edu.dental.domain.records.WorkRecordBook;
import edu.dental.web.JsonObjectParser;
import jakarta.servlet.http.HttpServletRequest;

import java.util.Arrays;
import java.util.Iterator;

import static edu.dental.web.builders.HtmlTag.*;

public class WorkTableBodyFunction {

    public WorkTableBodyFunction(HttpServletRequest request) {
        String jsonMap = (String) request.getAttribute("map");
        ProductMapDTO mapDTO = (ProductMapDTO) JsonObjectParser.getInstance().parseFromJson(jsonMap, ProductMapDTO.class);
        this.map = mapDTO.getKeys();
        String jsonWorks = (String) request.getAttribute("works");
        DentalWorkDTO[] works = (DentalWorkDTO[]) JsonObjectParser.getInstance().parseFromJson(jsonWorks, DentalWorkDTO[].class);
        this.iterator = Arrays.stream(works).iterator();
    }

    private final String[] map;
    private final Iterator<DentalWorkDTO> iterator;

    public boolean hasNext() {
        return iterator.hasNext();
    }

    public String next() {
        DentalWorkDTO dw = iterator.next();
        StringBuilder str = new StringBuilder();
        HtmlTag tagA = dw.getStatus().equals(DentalWork.Status.MAKE.toString()) ? A_TR
                : dw.getStatus().equals(DentalWork.Status.CLOSED.toString()) ? A_TR_CLOSED
                : A_TR_PAID;
        str.append(String.format(tagA.o, dw.getId())).append("\n\t\t");
        DIV_TD.line(str, dw.getPatient());
        DIV_TD.line(str, dw.getClinic());
        if (dw.getProducts().length == 0) {
            for (String ignored : map) {
                DIV_TD.line(str, "");
            }
        } else {
            for (String s : map) {
                Product p = WorkRecordBook.findProduct(dw, s);
                DIV_TD.line(str, p == null ? " " : String.valueOf(p.quantity()));
            }
        }
        DIV_TD.line(str, dw.getComplete() != null ? String.valueOf(dw.getComplete()) : "");
        DIV_TD.line(str, String.valueOf(dw.getAccepted()));
        str.append(tagA.c);
        return str.toString();
    }
}
