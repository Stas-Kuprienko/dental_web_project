package edu.dental.web.builders;

public enum HtmlTag {

    DIV_TABLE("""
            <div class="table">""",
            "</div>"),

    DIV_TBODY("""
            <div class="tbody">""",
            "</div>"),

    DIV_TH("""
            <div class="th">""",
            "</div>"),

    A_TR("""
            <a class="tr" href="/dental/work-handle">""",
            "</a>"),

    A_TR_CLOSED("""
            <a class="tr-closed" href="/dental/work-handle">""",
            "</a>"),

    A_TR_PAID("""
            <a class="tr-paid" href="/dental/work-handle">""",
            "</a>"),

    DIV_TD("""
            <div class="td">""",
            "</div>"),

    /**
     * Necessary using by {@linkplain String#format(String, Object...)}.
     */
    OPTION("""
            <option value="%s">""",
            "</option>")
    ;


    public final String o;
    public final String c;

    public StringBuilder line(StringBuilder str, String value) {
        str.append(o).append(value).append(c).append("\n\t\t");
        return str;
    }

    HtmlTag(String o, String c) {
        this.o = o;
        this.c = c;
    }

    public enum SAMPLES {

        /**
         * Necessary using by {@linkplain String#format(String, Object...)}.
         */
        BASIC("""
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
                    <a href="dental/work-handle">WORK LIST</a>
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
                    <a href="dental/work-handle">WORK LIST</a>
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
        TABLE_MONTH("""
                <h3>
                    <strong>%s</strong>
                </h3>
                """);


        public final String sample;

        SAMPLES(String sample) {
            this.sample = sample;
        }
    }
}
