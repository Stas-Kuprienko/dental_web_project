<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
  <title>DENTAL MECHANIC SERVICE</title>
  <link href="/dental/css/style.css" rel="stylesheet" type="text/css"/>
</head>
<nav class="menu" style="height: 10%;">
  <header><strong>DENTAL MECHANIC SERVICE</strong></header>
</nav>
<body>
<section>
<h2>Sign up, please:</h2>
<form action="/dental/main" method="post">
    <label for="name">name:</label><br>
    <input type="text" id="name" name="name" value=""><br>
    <label for="email">email:</label><br>
    <input type="text" id="email" name="email" value=""><br>
    <label for="password">password:</label><br>
    <input type="password" id="password" name="password" value=""><br><br>
    <button type="submit" style="font-size: 18px; width: 95px;">SIGN UP</button>
</form>
<br><br>
<form action="/dental/">
    <button type="submit" style="font-size: 14px; width: 80px;">LOG IN</button>
</form>
</section>
</body>
</html>