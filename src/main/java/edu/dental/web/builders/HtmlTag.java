package edu.dental.web.builders;

import edu.dental.domain.DatesTool;
import edu.dental.domain.entities.I_DentalWork;
import edu.dental.domain.entities.Product;
import jakarta.servlet.http.HttpServletRequest;

import java.util.Iterator;
import java.util.List;

public enum HtmlTag {

    /**
     * Necessary using by {@linkplain String#format(String, Object...)}.
     */
    BASIC ("""
            <!DOCTYPE html>
            <html>
            <head>
                <meta charset="UTF-8">
                <title>DENTAL MECHANIC SERVICE</title>
                <link rel="stylesheet" type="text/css" href="../css/style.css">
            </head>
            <nav class="menu">
                <header><strong>DENTAL MECHANIC SERVICE</strong></header>
                <a href="/dental/new-work">NEW WORK</a>
                <a href="dental/work-list">WORK LIST</a>
                <a href="/dental/new-product">PRODUCT MAP</a>
                <a href="/dental/product-map">REPORTS</a>
            </nav>
            <body>
            <section>
                %s
            </section>
            </body>
            </html>
            """),

    /**
     * Necessary using by {@linkplain String#format(String, Object...)}.
     */
    BASIC_APP("""
            <!DOCTYPE html>
            <html>
            <head>
                <meta charset="UTF-8">
                <title>DENTAL MECHANIC SERVICE</title>
                <link rel="stylesheet" type="text/css" href="../css/style.css">
            </head>
            <nav class="menu">
                <header><strong>DENTAL MECHANIC SERVICE</strong></header>
                <a href="/dental/new-work">NEW WORK</a>
                <a href="dental/work-list">WORK LIST</a>
                <a href="/dental/new-product">PRODUCT MAP</a>
                <a href="/dental/product-map">REPORTS</a>
            </nav>
            <body>
            <section>
                %s
            </section>
            </body>
            </html>
            """),

    TABLE_STATUS("""
            <h4 style="float: left;">
                    <label style="background-color: #002d73; border: 5px solid #002d73">CLOSED</label>&emsp;
                    <label style="background-color: #075700; border: 5px solid #075700"> PAID </label>
                </h4>
            """),

    /**
     * Necessary using by {@linkplain String#format(String, Object...)}.
     */
    TABLE_THEAD("""
            <div class="thead">
                        <div class="tr">
                            <div class="th">ПАЦИЕНТ</div>
                            <div class="th">КЛИНИКА</div>
                            %s
                            <div class="th">СДАЧА</div>
                            <div class="th">ДОПОЛН</div>
                        </div>
                    </div>
            """),

    /**
     * Necessary using by {@linkplain String#format(String, Object...)}.
     */
    TABLE_MONTH ("""
            <h3>
                <strong>%s</strong>
            </h3>
            """);


    public final String tag;
    HtmlTag(String tag) {
        this.tag = tag;
    }

    public enum TAG2 {

        DIV_TABLE ("""
            <div class="table">""",
                "</div>"),

        DIV_TBODY ("""
                <div class="tbody">""",
                "</div>"),

        DIV_TH ("""
                <div class="th">""",
                "</div>"),

        /**
         * Necessary using by {@linkplain String#format(String, Object...)}.
         */
        A_TR ("""
                <a class="tr" href="/dental/edit-work?id=%s">""",
                "</a>"),

        /**
         * Necessary using by {@linkplain String#format(String, Object...)}.
         */
        A_TR_CLOSED ("""
            <a class="tr-closed" href="/dental/edit-work?id=%s">""",
                "</a>"),

        /**
         * Necessary using by {@linkplain String#format(String, Object...)}.
         */
        A_TR_PAID ("""
                <a class="tr-paid" href="/dental/edit-work?id=%s">""",
                "</a>"),

        DIV_TD ("""
                <div class="td">""",
                "</div>");


        public final String o;
        public final String c;
        TAG2(String o, String c) {
            this.o = o;
            this.c = c;
        }
    }

    public static final String TABLE_MAP = "map";
    public static final String WORK_LIST = "works";


    public static StringBuilder line(StringBuilder str, TAG2 tag, String value) {
        str.append(tag.o).append(value).append(tag.c).append("\n\t\t");
        return str;
    }

    public static String month() {
        String[] yearNMonth = DatesTool.getYearAndMonth();
        return yearNMonth[1].toUpperCase() + " - " + yearNMonth[0];
    }

    public static class Liner {

        private final String[] map;
        private int cursor;

        public Liner(HttpServletRequest request) {
            this.map = (String[]) request.getAttribute(TABLE_MAP);
            this.cursor = 0;
        }

        public boolean hasNext() {
            return cursor < map.length;
        }

        public String next() {
            String s = map[cursor];
            cursor += 1;
            return s;
        }
    }

    public static class RowBuilder {

        private final Iterator<I_DentalWork> works;
        private final String[] map;
        private int cursor;

        @SuppressWarnings("unchecked")
        public RowBuilder(HttpServletRequest request) {
            List<I_DentalWork> list = (List<I_DentalWork>) request.getAttribute(WORK_LIST);
            this.works = list.iterator();
            this.map = (String[]) request.getAttribute(TABLE_MAP);
        }

        public boolean hasNext() {
            return works.hasNext();
        }

        public String next() {
            I_DentalWork dw = works.next();
            StringBuilder str = new StringBuilder();
            TAG2 tagA = dw.getStatus().equals(I_DentalWork.Status.MAKE) ? TAG2.A_TR
                    : dw.getStatus().equals(I_DentalWork.Status.CLOSED) ? TAG2.A_TR_CLOSED
                                                                        : TAG2.A_TR_PAID;
            str.append(String.format(tagA.o, dw.getId())).append("\n\t\t");
            line(str, TAG2.DIV_TD, dw.getPatient());
            line(str, TAG2.DIV_TD, dw.getClinic());
            if (dw.getProducts().isEmpty()) {
                for (String ignored : map) {
                    line(str, TAG2.DIV_TD, " ");
                }
            } else {
                for (String s : map) {
                    Product p = dw.findProduct(s);
                    line(str, TAG2.DIV_TD, p == null ? " " : String.valueOf(p.quantity()));
                }
            }
            line(str, TAG2.DIV_TD, String.valueOf(dw.getComplete()));
            line(str, TAG2.DIV_TD, String.valueOf(dw.getAccepted()));
            str.append(tagA.c);
            return str.toString();
        }
    }
}
