<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ page isELIgnored = "false" %>

<html>
<head>
    <title>DENTAL MECHANIC SERVICE</title>
    <link href="/dental/css/style.css" rel="stylesheet" type="text/css"/>
</head>
<script src="http://code.jquery.com/jquery-latest.js"></script>
<script type="text/javascript">
	$(document).ready(function(){

		$('#name-label').click(function(){
			$('#name-form').toggle("show");
		});

	});
	$(document).ready(function(){

		$('#email-label').click(function(){
			$('#email-form').toggle("show");
		});
	});
	$(document).ready(function(){

		$('#password-label').click(function(){
			$('#password-form').toggle("show");
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
    <div class="work">
        <label style="font-size:80%;">USER:</label><br>
        <label id="name-label"> ${user.name} </label>
        <form id="name-form" method="post" action="/dental/main/account" style="display:none">
            <input type="text" name="value" value=""/>
            <input type="hidden" name="field" value="name">
            <input type="submit" value="save">
        </form>
    </div>
    <div class="work">
        <label style="font-size:80%;">EMAIL:</label><br>
        <label id="email-label"> ${user.email} </label>
        <form id="email-form" method="post" action="/dental/main/account" style="display:none">
            <input type="text" name="value" value=""/>
            <input type="hidden" name="field" value="email">
            <input type="submit" value="save">
        </form>
    </div>
    <div class="work">
        <label style="font-size:80%;">CREATED:</label><br>
        <label> ${user.created} </label>
    </div>
    <div class="work">
        <label id="password-label" style="font-size:80%;">change password</label><br>
        <form id="password-form" method="post" action="/dental/main/account" style="display:none">
            <input type="password" name="value" value=""/>
            <input type="hidden" name="field" value="password">
            <input type="submit" value="save" onclick="return confirm('CHANGE PASSWORD?')">
        </form>
    </div>
    <br><br>
    <a>
        <form method="post" action="/dental/main/account">
            <input type="submit" value="delete account" onclick="return confirm('DELETE YOUR ACCOUNT?')">
            <input type="hidden" name="method" value="delete">
        </form>
    </a>
</section>
</body>
</html>