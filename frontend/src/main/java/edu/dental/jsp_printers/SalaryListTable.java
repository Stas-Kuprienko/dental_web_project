package edu.dental.jsp_printers;

import edu.dental.beans.SalaryRecord;
import jakarta.servlet.http.HttpServletRequest;

import java.util.Arrays;
import java.util.Iterator;

import static edu.dental.jsp_printers.HtmlTag.*;

@SuppressWarnings("unused")
public class SalaryListTable {

    private static final String href = "work-list?year-month=";

    private final Iterator<SalaryRecord> iterator;


    public SalaryListTable(HttpServletRequest request) {
        SalaryRecord[] records = (SalaryRecord[]) request.getAttribute("salary");
        this.iterator = Arrays.stream(records).iterator();
    }

    public boolean hasNext() {
        return iterator.hasNext();
    }

    public String next() {
        SalaryRecord record = iterator.next();
        StringBuilder str = new StringBuilder();
        str.append(String.format(A_TR.o, href + record.year() + "-" + record.getMonthValue())).append("\n\t");
        DIV_TD.line(str, String.valueOf(record.year()));
        DIV_TD.line(str, record.month());
        DIV_TD.line(str, String.valueOf(record.amount()));
        str.append(A_TR.c);
        return str.toString();
    }
}
