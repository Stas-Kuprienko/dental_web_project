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
    <a href="/dental/main/account">ACCOUNT</a>
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
            <%=td.input_for_sorting_previous_month()%>
        </form>
    </div>
    <a>
        <form method="get" action="/dental/main/reports/download">
            <input class="low-button" type="submit" value="download">
            <%=td.hidden_input_for_downloading()%>
        </form>
    </a>
</nav>
</html>