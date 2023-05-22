<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<jsp:useBean id="cartItems" type="java.util.ArrayList" scope="request"/>
<tags:master pageTitle="Cart">
    </br>
    <c:if test="${not empty param.message && empty error}">
        <p class="message">
                ${param.message}
        </p>
    </c:if>
    <c:if test="${not empty error}">
        <p class="errorGeneral">
            An error occurred while updating cart
        </p>
    </c:if>
    <form method="post">
        <table>
            <thead>
            <tr>
                <td>Image</td>
                <td>Description</td>
                <td>Quantity</td>
                <td class="price">Price</td>
            </tr>
            </thead>
            <c:forEach var="cartItem" items="${cartItems}" varStatus="status">
                <tr>
                    <td>
                        <img class="product-tile" src="${cartItem.product.imageUrl}">
                    </td>
                    <td>
                        <a href="${pageContext.servletContext.contextPath}/products/${cartItem.product.id}">
                                ${cartItem.product.description}
                        </a>
                    </td>
                    <td>
                        <fmt:formatNumber value="${cartItem.quantity}" var="quantity"/>
                        <c:set var="error" value="${errors[cartItem.product.id]}"/>
                        <input name="quantity" value="${not empty error ? paramValues['quantity'][status.index] : cartItem.quantity}">
                        <c:if test="${not empty error}">
                            <p class="error">
                                ${errors[cartItem.product.id]}
                            </p>
                        </c:if>
                        <input name="productId" type="hidden" value="${cartItem.product.id}">
                    </td>
                    <td class="price">
                        <a href='#'
                           onclick='javascript:window.open("${pageContext.servletContext.contextPath}/priceHistory/${cartItem.product.id}", "_blank", "scrollbars=1,resizable=1,height=300,width=450");'
                           title='Pop Up'>
                            <fmt:formatNumber value="${cartItem.product.price}" type="currency"
                                              currencySymbol="${cartItem.product.currency.symbol}"/>
                        </a>
                    </td>
                </tr>
            </c:forEach>
        </table>
        <p>
            <button>Update</button>
        </p>
    </form>
</tags:master>
