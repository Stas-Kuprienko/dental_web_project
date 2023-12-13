<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="edu.dental.view.builders.WorkViewPage" %>
<% WorkViewPage view = new WorkViewPage(request); %>

<html>
<head>
  <title>DENTAL MECHANIC SERVICE</title>
  <link href="/dental/css/style.css" rel="stylesheet" type="text/css"/>
</head>
<nav class="menu">
  <header><strong>DENTAL MECHANIC SERVICE</strong></header>
  <a href="/dental/main/new-work">NEW WORK</a>
  <a href="/dental/main/work-handle">WORK LIST</a>
  <a href="/dental/main/product-map">PRODUCT MAP</a>
  <a href="/dental/main/reports">REPORTS</a>
</nav>
<script src="http://code.jquery.com/jquery-latest.js"></script>
<script type="text/javascript">
	$(document).ready(function(){

		$('#patient-label').click(function(){
			$('#patient-form').toggle();
		});

	});

	$(document).ready(function(){

		$('#clinic-label').click(function(){
			$('#clinic-form').toggle();
		});
	});

	$(document).ready(function(){

		$('#complete-label').click(function(){
			$('#complete-form').toggle();
		});
	});

	$(document).ready(function(){

		$('#status-label').click(function(){
			$('#status-form').toggle();
		});
	});

	$(document).ready(function(){

		$('#comment-label').click(function(){
			$('#comment-form').toggle();
		});
	});

	$(document).ready(function(){

		$('#product-label').click(function(){
			$('#add-product').toggle();
		});
	});

	</script>
<body>
<section style="font-size:24px;">
  <div class="work">
    <label id="patient-label" style="font-size:80%;">patient:</label><br>
    <label> <%=view.work.patient() %> </label>
    <form id="patient-form" action="/dental/main/work-edit" style="display:none">
      <input type="text" name="value" value=""/>
      <input type="hidden" name="field" value="patient">
      <%=view.inputId()%>
      <input type="submit" value="save">
    </form></div>
  <div class="work">
    <label id="clinic-label" style="font-size:80%;">clinic:</label><br>
    <label> <%=view.work.clinic() %> </label>
    <form id="clinic-form" action="/dental/main/work-edit" style="display:none">
      <input type="text" name="value" value=""/>
      <input type="hidden" name="field" value="clinic">
      <%=view.inputId()%>
      <input type="submit" value="save">
    </form></div>
  <div class="work">
    <label id="complete-label" style="font-size:80%;">complete:</label><br>
    <label> <%=view.work.complete() %> </label>
    <form id="complete-form" action="/dental/main/work-edit" style="display:none">
      <input type="date" name="value" value=""/>
      <input type="hidden" name="field" value="complete">
      <%=view.inputId()%>
      <input type="submit" value="save">
    </form></div>
  <div class="work">
    <label id="product-label" style="font-size:80%;">products:</label>
    <form id="add-product" action="/dental/main/work-edit" style="display:none;">
      <label for="product">product:</label>
      <select id="product" name="value">
        <option value=""></option>
        <% while(view.option.hasNext()) { %>
        <%=view.option.next()%>
        <% } %>
      </select>
      <label for="quantity">quantity:</label>
      <input style="width: 64px;" type="number" id="quantity" name="quantity" value="" max="32">
      <input type="hidden" name="field" value="product">
      <%=view.buttonId()%>
    </form>
    <form action="/dental/main/delete-item">
      <% while(view.hasNextProduct()) { %>
      <%=view.nextProduct()%>
      <% } %>
    </form>
  </div>  <div class="work">
  <label id="status-label" style="font-size:80%;">status:</label><br>
  <label> <%=view.work.status() %> </label>
  <form id="status-form" action="/dental/main/work-edit" style="display:none">
    <select name="value">
      <option value="MAKE">make</option>
      <option value="CLOSED">closed</option>
      <option value="PAID">paid</option>
    </select>
    <input type="hidden" name="field" value="status">
    <%=view.inputId()%>
    <input type="submit" value="save">
  </form></div>
  <div class="work">
    <label id="comment-label" style="font-size:80%;">comment:</label><br>
    <label> <%=view.work.comment() %> </label>
    <form id="comment-form" action="/dental/main/work-edit" style="display:none">
      <textarea name="value"></textarea>
      <input type="hidden" name="field" value="comment">
      <%=view.inputId()%>
      <input type="submit" value="save">
    </form></div>
  <div class="work">
    <label  style="font-size:80%;">created:</label><br>
     <%=view.work.accepted() %> <br><br>
    <form action="/dental/main/photo">
      <button style="width:auto;" type="submit">OPEN PHOTOS</button>
      <%=view.inputId()%>
    </form>
    <form action="/dental/main/delete-item">
      <button style="width:auto;"  type="submit" onclick="return confirm('Are you sure?')">DELETE</button>
      <%=view.inputId()%>
    </form>
  </div>
</section>
</body>
</html>