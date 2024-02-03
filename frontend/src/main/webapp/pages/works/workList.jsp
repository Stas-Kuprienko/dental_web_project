<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="edu.dental.tag_support.HeaderMonth" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="d" uri="/WEB-INF/MyTagDescriptor.tld" %>
<%@ page isELIgnored = "false" %>
<% HeaderMonth date = new HeaderMonth(request); %>
<% pageContext.setAttribute("date", date); %>

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

    		$('#profit-label').click(function(){
    			$('#profit-form').toggle("show");
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
    <a href="/dental/main/dental-works">WORK LIST</a>
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
            ${pageScope.date.month()}
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
                <d:forEach tag="div" style="th" items="${sessionScope.map}"/>
                <div class="th">COMPLETE</div>
                <div class="th">ACCEPT</div>
            </div>
        </div>
        <div class="tbody">
            <c:forEach items="${works}" var="record">
            <c:set var="work" value="${record}"/>
            <d:row tag="a" href="/dental/main/dental-work" work="${pageScope.work}">
                <div class="td"> ${work.patient} </div>
                <div class="td"> ${work.clinic} </div>
                <d:products tag="div" style="td" map="${sessionScope.map}" work="${pageScope.work}"/>
                <div class="td"> ${work.complete} </div>
                <div class="td"> ${work.accepted} </div>
            </d:row>
            </c:forEach>
        </div>
    </div>
</section>
</body>
<nav class="low-menu">
    <a id="profit-label">
        <button> count profit </button>
    </a>
    <div id="profit-form" style="display:none;">
        <form method="post" action="/dental/main/profit" style="float:left;">
            <input class="medium-button" type="submit" value="current">
            <input type="hidden" name="year" value="${pageScope.date.year}">
            <input type="hidden" name="month" value="${pageScope.date.monthValue}"> &emsp;
        </form>
        <form method="post" action="/dental/main/profit" style="float:left;">
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
        <form method="get" action="/dental/main/dental-works">
            <input type="month" name="year-month" value="${pageScope.date.year}-${pageScope.date.monthValue}">
            <input class="medium-button" type="submit" value="get">
        </form>
    </div>
    <a id="sort-label">
        <button> sorting </button>
    </a>
    <div id="sort-form" style="display:none;">
        <form method="get" action="/dental/main/dental-works/sort" style="float:left;">
            <input type="hidden" name="year" value="${pageScope.date.nowYear}">
            <input type="hidden" name="month" value="${pageScope.date.nowMonthValue}">
            <input class="medium-button" type="submit" value="current"> &emsp;
        </form>
        <form method="get" action="/dental/main/dental-works/sort" style="float:left;">
            <input type="hidden" name="year" value="${pageScope.date.prevYear}">
            <input type="hidden" name="month" value="${pageScope.date.prevMonthValue}">
            <input class="medium-button" type="submit" value="previous"> &emsp;
        </form>
        <form method="post" action="/dental/main/dental-works/sort" style="float:left;">
            <input type="hidden" name="year" value="${pageScope.date.year}">
            <input type="hidden" name="month" value="${pageScope.date.monthValue}">
            <input class="medium-button" type="submit" value="all paid">
        </form>
    </div>
    <a>
        <form method="get" action="/dental/main/reports/download">
            <input class="low-button" type="submit" value="download">
            <input type="hidden" name="year" value="${pageScope.date.year}">
            <input type="hidden" name="month" value="${pageScope.date.monthValue}">
        </form>
    </a>
</nav>
</html>