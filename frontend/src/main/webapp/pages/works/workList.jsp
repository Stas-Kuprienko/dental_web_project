<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="edu.dental.jsp_printers.WorkListTable" %>
<% WorkListTable td = new WorkListTable(request); %>

<html>
<head>
    <title>DENTAL MECHANIC SERVICE</title>
    <link href="/dental/css/style.css" rel="stylesheet" type="text/css"/>
</head>
<script src="http://code.jquery.com/jquery-latest.js"></script>
<script type="text/javascript">
	$(document).ready(function(){

		$('#sort-label').click(function(){
			$('#sort-form').toggle();
		});

	});
	$(document).ready(function(){

    		$('#monthly-label').click(function(){
    			$('#monthly-form').toggle();
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
        <strong>
            <%=td.month()%>
        </strong>
    </h3>
	<h4 style="float: left;">
        <label style="background-color: #002d73; border: 5px solid #002d73">CLOSED</label> &emsp;
        <label style="background-color: #075700; border: 5px solid #075700"> PAID </label>
    </h4>
	<div class="table">
	    <div class="thead" style="font-size: 65%;">
            <div class="tr">
            <div class="th">PATIENT</div>
            <div class="th">CLINIC</div>
            <% while (td.tableHead.hasNext()) {%>
            <div class="th">
                <%=td.tableHead.next()%>
            </div> <%} %>
            <div class="th">COMPLETE</div>
            <div class="th">ACCEPT</div>
        </div>
    </div>
    <div class="tbody">
    <% while (td.hasNext()) {%>
        <%=td.next()%> <%} %>
	</div></div>
</section><br>
    <div class="under-table">
        <a id="monthly-label"> GET BY MONTH </a><br><br>
        <a id="monthly-form" style="display:none;">
            <form action="/dental/main/work-list">
                <input type="month" name="year-month" value="2024-01">
                <input class="medium-button" style="height:auto;" type="submit" value="get">
            </form>
        </a>
    </div>
	<div class="under-table">
        <a id="sort-label"> SORTING </a><br><br>
        <a id="sort-form" style="display:none;">
            <form method="get" action="/dental/main/work-list/sort">
                <input class="medium-button" type="submit" value="current">
                <%=td.input_for_sorting_current_month()%>
            </form>
            <form method="get" action="/dental/main/work-list/sort">
                <input class="medium-button" type="submit" value="previous">
                <%=td.input_for_sorting_previous_month()%>
            </form>
        </a></div>
</body>
</html>