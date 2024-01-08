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

    		$('#search-label').click(function(){
    			$('#search-form').toggle("show");
    		});

    	});
	$(document).ready(function(){

    		$('#salary-label').click(function(){
    			$('#salary-form').toggle("show");
    		});

    	});
    $(document).ready(function(){

		$('#sort-label').click(function(){
			$('#sort-form').toggle("show");
		});

	});
	$(document).ready(function(){

    		$('#monthly-label').click(function(){
    			$('#monthly-form').toggle("show");
    		});

    	});
</script>
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
</section>
</body>
<nav class="low-menu">
    <a id="salary-label">
        <button> count salary </button>
    </a>
    <div id="salary-form" style="display:none;">
        <form method="post" action="/dental/main/salary" style="float:left;">
            <input class="medium-button" type="submit" value="current">
            <%=td.hidden_input_year_and_month()%> &emsp;
        </form>
        <form method="post" action="/dental/main/salary" style="float:left;">
            <input class="medium-button" type="submit" value="all time">
        </form>
    </div>
    <a id="search-label">
        <button> search record </button>
    </a>
    <div id="search-form" style="display:none;">
        <form method="post" action="/dental/main/dental-works/search" style="float:left;">
            <input type="text" name="patient" placeholder="patient" style="display:block;"> &emsp;
            <input type="text" name="clinic" placeholder="clinic" style="display:block;"> &emsp;
            <input class="medium-button" type="submit" value="find" style="display:block;">
        </form>
    </div>
    <a id="monthly-label">
        <button> another month </button>
    </a>
    <div id="monthly-form" style="display:none;">
        <form method="get" action="/dental/main/work-list">
            <%=td.form_get_works_by_month()%>
            <input class="medium-button" type="submit" value="get">
        </form>
    </div>
    <a id="sort-label">
        <button> sorting </button>
    </a>
    <div id="sort-form" style="display:none;">
        <form method="get" action="/dental/main/work-list/sort" style="float:left;">
            <input class="medium-button" type="submit" value="current">
            <%=td.input_for_sorting_current_month()%> &emsp;
        </form>
        <form method="get" action="/dental/main/work-list/sort" style="float:left;">
            <input class="medium-button" type="submit" value="previous">
            <%=td.input_for_sorting_previous_month()%> &emsp;
        </form>
        <form method="post" action="/dental/main/work-list/sort" style="float:left;">
            <input class="medium-button" type="submit" value="all paid">
            <%=td.hidden_input_year_and_month()%>
        </form>
    </div>
    <a>
        <form method="get" action="/dental/main/reports/download">
            <input class="low-button" type="submit" value="download">
            <%=td.hidden_input_year_and_month()%>
        </form>
    </a>
</nav>
</html>