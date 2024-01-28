<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ page isELIgnored = "false" %>

<html>
<head>
    <title>DENTAL MECHANIC SERVICE</title>
    <link href="/dental/css/style.css" rel="stylesheet" type="text/css"/>
</head>
<script src="http://code.jquery.com/jquery-latest.js"></script>
<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
<script src="https://code.jquery.com/ui/1.12.1/jquery-ui.js"></script>
<script type="text/javascript">
	$(document).ready(function(){

		$('#product-new').click(function(){
			$('#product-form').toggle("show");
		});

	});

$(document).ready(function() {
  $('#map .tr').click(function() {
    var rowId = $(this).data('rowid');
    openDialog(rowId);
  });
});

$(document).ready(function() {
  $('#map .tr').click(function() {
    var rowId = $(this).data('rowid');
    openDialog(rowId);
  });
});

function openDialog(rowId) {
  var content = 'Load data for row ' + rowId + ' and populate the form fields';

  $('#dialog').dialog({
    title: '*  update  *',
    modal: true,
    buttons: {
      Save: function() {
        var editedData = {
          name: $('#key').val(),
          price: $('#value').val(),
        };

        $.ajax({
          url: '/dental/main/product-map/' + rowId,
          type: 'PUT',
          data: editedData,
          success: function(response) {
            console.log('PUT request successful');
            console.log(response);
          },
          error: function(xhr, status, error) {
            console.log('PUT request failed');
            console.log(xhr.responseText);
          }
        });

        window.location.href = '/dental/main/product-map/' + rowId;
      },
    Delete: function() {
      $.ajax({
        url: '/dental/main/product-map/' + rowId,
        type: 'DELETE',
        success: function(response) {
          console.log('DELETE request successful');
          console.log(response);
        },
        error: function(xhr, status, error) {
          console.log('DELETE request failed');
          console.log(xhr.responseText);
        }
      });

        alert('delete the item?');
        window.location.href = '/dental/main/product-map/' + rowId;
      }
    },
    open: function() {
      $('#key').val().prop('disabled', true);
    }
  });
}
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
        <div class="tbody" id="map">
            <c:forEach items="${sessionScope.map.items}" var="item">
            <a class="tr" data-rowid="${item.id}">
                <div class="td">${item.key}</div>
                <div class="td">${item.value}</div>
            </a>
            </c:forEach>
        </div>
    </div>
    <div class="th" id="dialog" style="display:none;">
        <form>
            <label for="key"></label>
            <input type="text" id="key" name="key" readonly>
            <br>
            <label for="value"></label>
            <input type="number" id="value" name="value">
            <br>
        </form>
    </div>
</section>
</body>
</html>