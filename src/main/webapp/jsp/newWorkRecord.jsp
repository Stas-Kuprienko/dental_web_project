<%@ page import="edu.dental.web.builders.PageBuilder" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<% PageBuilder.OptionBuilder option = new PageBuilder.OptionBuilder(request); %>
<html>
<head>
    <title>DENTAL MECHANIC SERVICE</title>
    <link href="/dental/css/style.css" rel="stylesheet" type="text/css"/>
</head>
<nav class="menu">
    <header><strong>DENTAL MECHANIC SERVICE</strong></header>
    <a href="/dental/main/new-work">NEW WORK</a>
    <a href="/dental/main/work-handle">WORK LIST</a>
    <a href="/dental/main/product-map">PRODUCT MAP</a>
    <a href="/dental/main/reports">REPORTS</a>
    <a style="float: right;" href="/dental/main/user">
        <button style="height: 50%; width: 100%; font-size: 20px;">
            ACCOUNT
        </button>
    </a>
</nav>
<body>
<section>
<h2>Input new record:</h2>
<form action="/dental/main/save-work" method="post">
    <label for="patient">patient:</label><br>
    <input type="text" id="patient" name="patient" value=""><br>
    <label for="clinic">clinic:</label><br>
    <input type="text" id="clinic" name="clinic" value=""><br>
    <label for="product">product:</label><br>
    <select id="product" name="product" style="width: 230px; height: 30px; font-size: 22px;">
        <option value=""></option>
        <% while(option.hasNext()) { %>
        <%=option.next()%>
        <% } %>
    </select><br>
    <label for="quantity">quantity:</label><br>
    <input type="number" id="quantity" name="quantity" value="0" max="32"><br>
    <label for="complete">complete:</label><br>
    <input type="date" id="complete" name="complete" style="width: 180px; height: 30px; font-size: 18px;" value=""><br>
    <br>
    <button type="submit" style="font-size: 18px; width: 90;">SAVE</button>
</form>
</section>
</body>
</html>