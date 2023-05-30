<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<jsp:useBean id="order" type="com.es.phoneshop.model.Order" scope="request"/>
<tags:master pageTitle="Order Overview">
    <h1>Order overview</h1>
    <table>
        <thead>
        <tr>
            <td>Image</td>
            <td>Description</td>
            <td>Quantity</td>
            <td class="price">Price</td>
        </tr>
        </thead>
        <c:forEach var="cartItem" items="${order.cartItems}" varStatus="status">
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
                        ${quantity}
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
        <tr>
            <td colspan="3">Subtotal:</td>
            <td colspan="2">
                <fmt:formatNumber value="${order.subtotal}" type="currency" currencySymbol="$"/>
            </td>
        </tr>
        <tr>
            <td colspan="3">Delivery cost:</td>
            <td colspan="2">
                <fmt:formatNumber value="${order.deliveryCost}" type="currency" currencySymbol="$"/>
            </td>
        </tr>
        <tr>
            <td colspan="3">Total cost:</td>
            <td colspan="2">
                <fmt:formatNumber value="${order.totalCost}" type="currency" currencySymbol="$"/>
            </td>
        </tr>
    </table>
    <h1>Your details</h1>
    <table>
        <tags:orderOverviewRow name="firstName" label="First Name" order="${order}"></tags:orderOverviewRow>
        <tags:orderOverviewRow name="lastName" label="Last Name" order="${order}"></tags:orderOverviewRow>
        <tags:orderOverviewRow name="phone" label="Phone" order="${order}"></tags:orderOverviewRow>
        <tags:orderOverviewRow name="deliveryAddress" label="Delivery Address" order="${order}"></tags:orderOverviewRow>
        <tags:orderOverviewRow name="deliveryDate" label="Delivery Date" order="${order}"></tags:orderOverviewRow>
        <tags:orderOverviewRow name="paymentMethod" label="Payment Method" order="${order}"></tags:orderOverviewRow>
    </table>
</tags:master>
