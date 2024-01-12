<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="edu.dental.jsp_printers.ProfitListPrinter" %>
<% ProfitListPrinter td = new ProfitListPrinter(request); %>
<html>
<head>
    <title>DENTAL MECHANIC SERVICE</title>
    <link href="/dental/css/style.css" rel="stylesheet" type="text/css"/>
</head>
<nav class="menu">
    <header><strong>DENTAL MECHANIC SERVICE</strong></header>
    <a href="/dental/main/new-work">NEW WORK</a>
    <a href="/dental/main/work-list">WORK LIST</a>
    <a href="/dental/main/product-map">PRODUCT MAP</a>
    <a href="/dental/main/account">ACCOUNT</a>
    <a style="float:right;" href="/dental/main/log-out" onclick="return confirm('LOG OUT?')">
            <button>log out</button>
        </a>
</nav>
<body>
<section>
    <div class="table">
        <div class="thead" style="font-size: 65%;">
            <div class="tr">
                <div class="th">YEAR</div>
                <div class="th">MONTH</div>
                <div class="th">PROFIT</div>
            </div>
        </div>
        <div class="tbody">
            <% while (td.hasNext()) {%>
            <%=td.next()%> <%} %>
        </div>
    </div>
</section>
</body>
<nav class="low-menu">
    <a>
        <form method="get" action="/dental/main/profit">
            <input class="low-button" type="submit" value="download statistics">
        </form>
    </a>
</nav>
</html>