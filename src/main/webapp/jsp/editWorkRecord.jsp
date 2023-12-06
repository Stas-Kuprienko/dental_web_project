<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<html>
<head>
    <title>DENTAL MECHANIC SERVICE</title>
    <link href="/dental/css/style.css" rel="stylesheet" type="text/css"/>
</head>
<nav class="menu">
    <header><strong>DENTAL MECHANIC SERVICE</strong></header>
    <a href="/dental/main/new-work">NEW WORK</a>
    <a href="/dental/main/work-handle">WORK LIST</a>
    <a href="/dental/main/new-product">PRODUCT MAP</a>
    <a href="/dental/main/product-map">REPORTS</a>
    <a style="float: right;" href="/dental/main/user">
        <button style="height: 50%; width: 100%; font-size: 20px;">
            ACCOUNT
        </button>
    </a>
</nav>
<body>
<section>
    <h4>Edit record:</h4>
    <form action="/dental/main/work-handle">
        <p class="center">
            <label  for="patient">PATIENT:</label><br>
            <input type="text" id="patient" name="patient" value="Иванов">
            <input type="hidden" name="field" value="patient">
            <button type="submit" name="id" value="2">input</button>
        </p>
    </form>
    <form action="/dental/main/work-handle">
        <p class="center">
            <label for="clinic">CLINIC:</label><br>
            <input type="text" id="clinic" name="clinic" value="Dental-Med">
            <input type="hidden" name="field" value="clinic">
            <button type="submit" name="id" value="2">input</button>
        </p>
    </form>
    <form action="/dental/main/work-handle">
        <p class="center">
            <label for="complete">COMPLETE:</label><br>
            <input type="date" id="complete" name="complete" value="2023-11-20">
            <input type="hidden" name="field" value="complete">
            <button type="submit" name="id" value="2">input</button>
        </p>
    </form>
    <form action="/dental/main/work-handle">
        <a class="center_list">
            <label for="product">PRODUCT:</label><br>
            <select id="product" name="product">
                <option value=""></option>
                <option value="керамика">керамика</option>
                <option value="zro2">zro2</option>
                <option value="emax">emax</option>
                <option value="работа">работа</option>
            </select><br>
            <label for="quantity">QUANTITY:</label><br>
            <input style="width: 64px;" type="number" id="quantity" name="quantity" value="" max="32"><br>
            <input type="hidden" name="field" value="product">
            <button type="submit" name="id" value="2">add</button>
        </a>
    </form>
    <form action="/dental/main/delete-element">
        <div class="center_list">
            <a class="tr">
                <div class="td" style="width: 100%;">керамика - 12</div>
                <button type="submit" name="product" value="керамика">delete</button>
            </a>
            <a class="tr">
                <div class="td" style="width: 100%;">zro2 - 6</div>
                <button type="submit" name="product" value="zro2">delete</button>
            </a>
            <a class="tr">
                <div class="td" style="width: 100%;">emax - 2</div>
                <button type="submit" name="product" value="emax">delete</button>
            </a>
        </div><br>
        <input type="hidden" name="id" value="2">
    </form>
    <form action="/dental/main/work-handle">
        <a class="center_list">
            <input type="radio" id="make" name="status" value="make" checked>
            <label for="make">MAKE</label>
            <input type="radio" id="closed" name="status" value="closed">
            <label for="closed">CLOSED</label>
            <input type="radio" id="paid" name="status" value="paid">
            <label for="paid">PAID</label>&emsp;
            <input type="hidden" name="field" value="status">
            <button type="submit" name="id" value="2">input</button>
        </a>
    </form>
    <form action="/dental/main/work-handle">
        <a class="center">
            <label for="comment">COMMENT:</label><br>
            <textarea id="comment" rows="3" cols="36" name="comment" maxlength="127"> </textarea>
            <input type="hidden" name="field" value="comment">
            <button type="submit" name="id" value="2">input</button>
        </a>
    </form>
    <a class="center">
        created:<br>
        10-11-2023
    </a>
    <form action="/dental/main/photo">
        <button type="submit" class="big-button">OPEN PHOTOS</button>
        <input type="hidden" name="id" value="2">
    </form>
    <form action="/dental/main/delete-element">
        <button type="submit" class="big-button">DELETE</button>
        <input type="hidden" name="id" value="2">
    </form>
</section>
</body>
</html>
