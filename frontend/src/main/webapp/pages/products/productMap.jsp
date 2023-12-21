<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="edu.dental.constructors.ProductMapTable" %>
<% ProductMapTable mapTable = new ProductMapTable(request); %>

<html>
<head>
    <title>DENTAL MECHANIC SERVICE</title>
    <link href="/dental/css/style.css" rel="stylesheet" type="text/css"/>
</head>
<script src="http://code.jquery.com/jquery-latest.js"></script>
<script type="text/javascript">
	$(document).ready(function(){

		$('#product-new').click(function(){
			$('#product-form').toggle();
		});

	});
</script>
<nav class="menu">
    <header><strong>DENTAL MECHANIC SERVICE</strong></header>
    <a href="/dental/main/new-work">NEW WORK</a>
    <a href="/dental/main/work-list">WORK LIST</a>
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
    <h3>
        <a class="tr" id="product-new">+ New product</a>
        <a class="th" id="product-form" style="display:none">
            <form method="post" action="/dental/main/product-map">
                <label for="title">product:</label>
                <input id="title" type="text" name="title" value=""><br>
                <label for="price">price:</label>
                <input style="width:25%;" id="price" type="number" name="price" value="">
                <input style="width:25%;" type="submit" value="save">
            </form>
        </a>
    </h3>
    <div class="table" style="width:75%;">
        <div class="thead">
            <div class="tr">
                <div class="th">PRODUCT</div>
                <div class="th">PRICE</div>
            </div>
        </div>
        <div class="tbody">
            <% while(mapTable.hasNext()) { %>
            <%=mapTable.next()%>
            <% } %>
        </div></div>
</section>
</body>
</html>