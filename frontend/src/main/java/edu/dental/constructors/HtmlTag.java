package edu.dental.constructors;

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

    A_TR_WITHOUT_HREF("""
            <a class="tr">""",
            "</a>"),

    /**
     * Necessary using by {@linkplain String#format(String, Object...)}.
     */
    A_TR("""
            <a class="tr" href="/dental/main/%s?id=%s">""",
            "</a>"),

    /**
     * Necessary using by {@linkplain String#format(String, Object...)}.
     */
    A_TR_CLOSED("""
            <a class="tr-closed" href="/dental/main/%s?id=%s">""",
            "</a>"),

    /**
     * Necessary using by {@linkplain String#format(String, Object...)}.
     */
    A_TR_PAID("""
            <a class="tr-paid" href="/dental/main/%s?id=%s">""",
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

    HtmlTag(String o, String c) {
        this.o = o;
        this.c = c;
    }

    public StringBuilder line(StringBuilder str, String value) {
        str.append(o).append(value).append(c).append("\n\t\t");
        return str;
    }

    public enum WORK_VIEW {

        /**
         * Necessary using by {@linkplain String#format(String, Object...)}.
         */
        INPUT_ID("""
                <input type="hidden" name="id" value="%s">"""),

        /**
         * Necessary using by {@linkplain String#format(String, Object...)}.
         */
        BUTTON_ID("""
                <button type="submit" name="id" value="%s">add</button>"""),

        /**
         * Necessary using by {@linkplain String#format(String, Object...)}.
         */
        PRODUCT_LIST("""
                <a class="tr">
                  <div class="td" style="width: 100%%;">%s</div>
                  <input type="hidden" name="id" value="%s">
                  <button type="submit" name="product" value="%s" onclick="return confirm('Are you sure?')">delete</button>
                </a>""");

        public final String sample;

        WORK_VIEW(String sample) {
            this.sample = sample;
        }

    }

    public enum PRODUCT_VIEW {
        FORM("""
            <div class="td" style="width: 40%%;">
            <form style="display: inline-block;" method="post" action="/dental/main/product-map">
                <input style="width:96px;" type="number" name="price" value="">
                <input type="submit" value="save">
                <input type="hidden" name="title" value="%s">
                <input type="hidden" name="id" value="%s">
                <input type="hidden" name="method" value="put">
            </form>&emsp;
            <form style="display: inline-block;" method="post" action="/dental/main/product-map">
                <input type="hidden" name="title" value="%s">
                <input type="hidden" name="id" value="%s">
                <input type="hidden" name="method" value="delete">
                <input type="submit" value="delete">
                </form>&emsp;
            <form style="display: inline-block;" action="/dental/main/product-map">
                <input type="submit" value="cancel">
            </form></div>""");


        public final String sample;
        PRODUCT_VIEW(String sample) {
            this.sample = sample;
        }
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
