<%--
  Created by IntelliJ IDEA.
  User: user
  Date: 10.05.2023
  Time: 13:34
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<html>
<head>
    <title>Price History</title>
</head>
<body>
<jsp:useBean id="priceHistory" type="java.util.ArrayList" scope="request"/>
<jsp:useBean id="product" type="com.es.phoneshop.model.Product" scope="request"/>
<h1>Price history</h1>
<h3>${product.description}</h3>
<table>
    <thead>
    <tr>
        <td>StartDate</td>
        <td>Price</td>
    </tr>
    </thead>
    <c:forEach var="priceHistory" items="${priceHistory}">
        <tr>
            <td>${priceHistory.date}</td>
            <td class="price">
                <fmt:formatNumber value="${priceHistory.price}" type="currency" currencySymbol="${priceHistory.currency.symbol}"/>
            </td>
        </tr>
    </c:forEach>
</table>
</body>
</html>
