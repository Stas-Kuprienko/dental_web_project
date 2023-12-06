<%@ page contentType="text/html;charset=UTF-8" language="java" %>
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
    <a href="/dental/main/reports">REPORTS</a>
</nav>
<body>
<section>
<h2>Input new product:</h2>
<form action="/dental/app/save-product" method="post">
    <label for="title">product:</label><br>
    <input type="text" id="title" name="title" value=""><br>
    <label for="price">price:</label><br>
    <input type="number" id="price" name="price" value=""><br>
    <br>
    <button type="submit" style="font-size: 18px; width: 90;">SAVE</button>
</form>
</section>
</body>
</html>