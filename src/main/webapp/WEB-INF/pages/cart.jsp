<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<jsp:useBean id="cartItems" type="java.util.ArrayList" scope="request"/>
<tags:master pageTitle="Cart">
    </br>
    <form method="post" action="">
        <c:forEach var="cartItem" items="${cartItems}">
            <table>
                <tr>
                    <td>Image</td>
                    <td><img src="${cartItem.product.imageUrl}"></td>
                </tr>
                <tr>
                    <td>Code</td>
                    <td>${cartItem.product.code}</td>
                </tr>
                <tr>
                    <td>Quantity</td>
                    <td>${cartItem.quantity}</td>
                </tr>
                <tr>
                    <td>Price</td>
                    <td>
                        <fmt:formatNumber value="${cartItem.product.price}" type="currency"
                                          currencySymbol="${cartItem.product.currency.symbol}"/>
                    </td>
                </tr>
            </table>
        </c:forEach>
    </form>
</tags:master>
