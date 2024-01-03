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
	<div class="sorting">
        <a class="button" id="sort-label"> SORTING </a><br><br>
        <a class="button" id="sort-form" style="display:none">
            <%=td.form_for_sorting_current_month()%>
                <input class="medium-button" type="submit" value="current">
            </form>
            <%=td.form_for_sorting_previous_month()%>
                <input class="medium-button" type="submit" value="previous">
            </form>
        </a></div>
</body>
</html>