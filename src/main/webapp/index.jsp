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
  <h2>Log in, please:</h2>
  <form action="/dental/log-in" method="post">
    <label for="email">email:</label><br>
    <input type="text" id="email" name="email" value=""><br>
    <label for="password">password:</label><br>
    <input type="password" id="password" name="password" value=""><br><br>
    <button type="submit" style="font-size: 18px; width: 90;">LOG IN</button>
  </form>
  <br><br>
  <form action="/dental/sign-up">
    <button type="submit" style="font-size: 14px; width: 80;">SIGN UP</button>
  </form>
</section>
</body>
</html>