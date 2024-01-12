package edu.dental.jsp_printers;

import edu.dental.beans.ProfitRecord;
import jakarta.servlet.http.HttpServletRequest;

import java.util.Arrays;
import java.util.Iterator;

import static edu.dental.jsp_printers.HtmlTag.*;

@SuppressWarnings("unused")
public class ProfitListPrinter {

    private static final String href = "work-list?year-month=";

    private final Iterator<ProfitRecord> iterator;


    public ProfitListPrinter(HttpServletRequest request) {
        ProfitRecord[] records = (ProfitRecord[]) request.getAttribute("profit");
        this.iterator = Arrays.stream(records).iterator();
    }

    public boolean hasNext() {
        return iterator.hasNext();
    }

    public String next() {
        ProfitRecord record = iterator.next();
        StringBuilder str = new StringBuilder();
        str.append(String.format(A_TR.o, href + record.year() + "-" + record.getMonthValue())).append("\n\t");
        DIV_TD.line(str, String.valueOf(record.year()));
        DIV_TD.line(str, record.month());
        DIV_TD.line(str, String.valueOf(record.amount()));
        str.append(A_TR.c);
        return str.toString();
    }
}
