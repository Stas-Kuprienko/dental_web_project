<%@ page import="edu.dental.domain.DatesTool" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>DENTAL MECHANIC SERVICE</title>
    <link href="/dental/css/style.css" rel="stylesheet" type="text/css"/>
</head>
<nav class="menu">
    <header><strong>DENTAL MECHANIC SERVICE</strong></header>
    <a href="/dental/main/new-work">NEW WORK</a>
    <a href="dental/main/work-list">WORK LIST</a>
    <a href="/dental/main/new-product">PRODUCT MAP</a>
    <a href="/dental/main/product-map">REPORTS</a>
</nav>
<body>
<section>
    <h3>
        <strong>
            <%String[] yearAndMonth = DatesTool.getYearAndMonth();%>
            <%=yearAndMonth[1].toUpperCase() + " - " + yearAndMonth[0]%>
        </strong>
    </h3>
	<h4 style="float: left;">
        <label style="background-color: #002d73; border: 5px solid #002d73">CLOSED</label>&emsp;
        <label style="background-color: #075700; border: 5px solid #075700"> PAID </label>
    </h4>

	<div class="table">
	    <div class="thead">
            <div class="tr">
            <div class="th">PATIENT</div>
            <div class="th">CLINIC</div>
            <% String[] map = (String[]) request.getAttribute("map"); %>
            <% for(String s : map) { %>
            <div class="th"><%=s%></div>
            <% } %>
            <div class="th">COMPLETE</div>
            <div class="th">ACCEPT</div>
        </div>
    </div>
<div class="tbody">
	<a class="tr" href="/dental/edit-work?id=1">
	<div class="td">Рабинович</div>
		<div class="td">Java-Dental</div>
		<div class="td">12</div>
		<div class="td"> </div>
		<div class="td"> </div>
		<div class="td"> </div>
		<div class="td">2023-11-15</div>
		</a>
	<a class="tr-closed" href="/dental/edit-work?id=2">
	<div class="td">���</div>
		<div class="td">����������</div>
		<div class="td"> </div>
		<div class="td"> </div>
		<div class="td"> </div>
		<div class="td"> </div>
		<div class="td">2023-11-15</div>
		</a>
	<a class="tr" href="/dental/edit-work?id=3">
	<div class="td">������</div>
		<div class="td">Dental-Med</div>
		<div class="td"> </div>
		<div class="td">7</div>
		<div class="td"> </div>
		<div class="td"> </div>
		<div class="td">2023-11-20</div>
		</a>
	<a class="tr-paid" href="/dental/edit-work?id=4">
	<div class="td">��������� ��������</div>
		<div class="td">������-����</div>
		<div class="td">8</div>
		<div class="td"> </div>
		<div class="td">4</div>
		<div class="td"> </div>
		<div class="td">2023-11-25</div>
		</a>
	<a class="tr" href="/dental/edit-work?id=5">
	<div class="td">��������</div>
		<div class="td">����</div>
		<div class="td"> </div>
		<div class="td">10</div>
		<div class="td">4</div>
		<div class="td"> </div>
		<div class="td">2023-11-20</div>
		</a>
	<a class="tr" href="/dental/edit-work?id=6">
	<div class="td">���-��</div>
		<div class="td">����</div>
		<div class="td">4</div>
		<div class="td"> </div>
		<div class="td"> </div>
		<div class="td"> </div>
		<div class="td">2023-11-25</div>
		</a>
	<a class="tr" href="/dental/edit-work?id=7">
	<div class="td">��������</div>
		<div class="td">������������</div>
		<div class="td"> </div>
		<div class="td"> </div>
		<div class="td"> </div>
		<div class="td"> </div>
		<div class="td">2023-12-01</div>
		</a>
	<a class="tr" href="/dental/edit-work?id=8">
	<div class="td">���������</div>
		<div class="td">�������</div>
		<div class="td"> </div>
		<div class="td"> </div>
		<div class="td"> </div>
		<div class="td"> </div>
		<div class="td">2023-11-25</div>
		</a>
	<a class="tr" href="/dental/edit-work?id=9">
	<div class="td">����</div>
		<div class="td">�������</div>
		<div class="td">10</div>
		<div class="td"> </div>
		<div class="td"> </div>
		<div class="td"> </div>
		<div class="td">2023-12-01</div>
		</a>
	</div></div>
</section>
</body>
</html>