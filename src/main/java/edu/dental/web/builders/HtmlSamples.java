package edu.dental.web.builders;

public enum HtmlSamples {

    /**
     * Necessary using by {@linkplain String#format(String, Object...)}.
     */
    BASIC ("""
            <!DOCTYPE html>
            <html>
            <head>
                <meta charset="UTF-8">
                <title>DENTAL MECHANIC SERVICE</title>
                <link rel="stylesheet" type="text/css" href="css/style.css">
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
    TABLE_THREAD ("""
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
    HtmlSamples(String tag) {
        this.tag = tag;
    }

    public enum tag2 {

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
        tag2(String o, String c) {
            this.o = o;
            this.c = c;
        }
    }

    public static StringBuilder line(StringBuilder str, tag2 tag, String value) {
        str.append(tag.o).append(value).append(tag.c).append("\n\t\t");
        return str;
    }
}
