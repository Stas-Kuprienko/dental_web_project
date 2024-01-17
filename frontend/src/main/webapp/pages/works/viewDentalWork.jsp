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

		$('#patient-label').click(function(){
			$('#patient-form').toggle("show");
		});

	});

	$(document).ready(function(){

		$('#clinic-label').click(function(){
			$('#clinic-form').toggle("show");
		});
	});

	$(document).ready(function(){

		$('#complete-label').click(function(){
			$('#complete-form').toggle("show");
		});
	});

	$(document).ready(function(){

		$('#status-label').click(function(){
			$('#status-form').toggle("show");
		});
	});

	$(document).ready(function(){

		$('#comment-label').click(function(){
			$('#comment-form').toggle("show");
		});
	});

	$(document).ready(function(){

		$('#product-label').click(function(){
			$('#add-product').toggle("show");
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
<section style="font-size:24px;">
  <div class="work">
    <label style="font-size:80%;">patient:</label><br>
    <label id="patient-label"> ${work.patient} </label>
    <form id="patient-form" method="post" action="/dental/main/dental-work" style="display:none">
      <input type="text" name="value" value=""/>
      <input type="hidden" name="field" value="patient">
      <input type="hidden" name="method" value="put">
      <input type="hidden" name="id" value="${work.id}">
      <input type="submit" value="save">
    </form></div>
  <div class="work">
    <label style="font-size:80%;">clinic:</label><br>
    <label id="clinic-label"> ${work.clinic} </label>
    <form id="clinic-form" method="post" action="/dental/main/dental-work" style="display:none">
      <input type="text" name="value" value=""/>
      <input type="hidden" name="field" value="clinic">
      <input type="hidden" name="method" value="put">
      <input type="hidden" name="id" value="${work.id}">
      <input type="submit" value="save">
    </form></div>
  <div class="work">
    <label style="font-size:80%;">complete:</label><br>
    <label id="complete-label"> ${work.complete} </label>
    <form id="complete-form" method="post" action="/dental/main/dental-work" style="display:none">
      <input type="date" name="value" value=""/>
      <input type="hidden" name="field" value="complete">
      <input type="hidden" name="method" value="put">
      <input type="hidden" name="id" value="${work.id}">
      <input type="submit" value="save">
    </form></div>
  <div class="work">
    <a><label> PRODUCTS: </label></a><br>
    <a class="tr">
        <label id="product-label" class="td"> + new product </label>
    </a>
    <form id="add-product" method="post" action="/dental/main/dental-work" style="display:none;">
      <label for="product">product:</label>
      <select id="product" name="value">
        <option value=""></option>
        <c:forEach items="${requestScope.map}" var="title">
        <option value="${title}"> ${title} </option>
        </c:forEach>
      </select>
      <label for="quantity">quantity:</label>
      <input style="width: 64px;" type="number" id="quantity" name="quantity" value="" max="32">
      <input type="hidden" name="field" value="product">
      <input type="hidden" name="method" value="put">
      <button type="submit" name="id" value="${work.id}">add</button>
    </form>
    <form method="post" action="/dental/main/dental-work">
      <c:forEach items="${work.products}" var="product">
        <a class="tr">
        <div class="td" style="width: 100%;"> ${product.title} - ${product.quantity} </div>
        <input type="hidden" name="id" value="${work.id}">
        <button type="submit" name="product" value="${product.title}" onclick="return confirm('Are you want to delete product?')">
            delete
        </button></a>
        </c:forEach>
      <input type="hidden" name="method" value="delete"><br>
    </form>
  </div>
  <div class="work">
  <label style="font-size:80%;">status:</label><br>
  <label id="status-label"> ${work.status} </label>
  <form id="status-form" method="post" action="/dental/main/dental-work" style="display:none">
    <select name="value">
      <option value="MAKE">make</option>
      <option value="CLOSED">closed</option>
      <option value="PAID">paid</option>
    </select>
    <input type="hidden" name="field" value="status">
    <input type="hidden" name="method" value="put">
    <input type="hidden" name="id" value="${work.id}">
    <input type="submit" value="save">
  </form></div>
  <div class="work">
    <label style="font-size:80%;">comment:</label> &emsp;
    <button id="comment-label">input</button><br>
    <label> ${work.comment} </label>
    <form id="comment-form" method="post" action="/dental/main/dental-work" style="display:none">
      <textarea name="value">
        ${work.comment}
      </textarea>
      <input type="hidden" name="field" value="comment">
      <input type="hidden" name="method" value="put">
      <input type="hidden" name="id" value="${work.id}">
      <input type="submit" value="save">
    </form></div>
  <div class="work">
    <label  style="font-size:80%;">created:</label><br>
     ${work.accepted} <br><br>
    <form action="/dental/main/photo" style="display:none">
      <button style="width:auto;" type="submit">OPEN PHOTOS</button>
      <input type="hidden" name="id" value="${work.id}">
    </form>
    <form method="post" action="/dental/main/dental-work">
      <button style="width:auto;"  type="submit" onclick="return confirm('Are you want to delete record?')">DELETE</button>
      <input type="hidden" name="id" value="${work.id}">
      <input type="hidden" name="method" value="delete">
    </form>
  </div>
</section>
</body>
</html>