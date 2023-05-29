<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<jsp:useBean id="order" type="com.es.phoneshop.model.Order" scope="request"/>
<tags:master pageTitle="Checkout">
    </br>
    <p>
        Total quantity: ${order.totalQuantity}
    </p>
    <c:if test="${not empty param.message && empty error}">
        <p class="message">
                ${param.message}
        </p>
    </c:if>
    <c:if test="${not empty error}">
        <p class="errorGeneral">
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
            <tags:orderFormRow name="firstName" label="First Name" order="${order}"
                               errors="${errors}"></tags:orderFormRow>
            <tags:orderFormRow name="lastName" label="Last Name" order="${order}"
                               errors="${errors}"></tags:orderFormRow>
            <tags:orderFormRow name="phone" label="Phone" order="${order}" errors="${errors}"></tags:orderFormRow>
            <tags:orderFormRow name="deliveryAddress" label="Delivery Address" order="${order}"
                               errors="${errors}"></tags:orderFormRow>
            <tr>
                <td>Delivery Date<span style="color: red;">*</span></td>
                <td>
                    <c:set var="error" value="${errors['deliveryDate']}"></c:set>
                    <input id="date" name="deliveryDate" value="${not empty error ? param['deliveryDate'] : order.deliveryDate}"
                           type="date">
                    <c:if test="${not empty error}">
                        <p style="color: red;">${error}</p>
                    </c:if>
                </td>
            </tr>
            <tr>
                <td>Payment Method<span style="color: red;">*</span></td>
                <td>
                    <select name="paymentMethod">
                        <option></option>
                        <c:forEach var="paymentMethod" items="${paymentMethods}">
                            <option value="${paymentMethod}"
                                    <c:if test="${paymentMethod eq param['paymentMethod']}">
                                        selected
                                    </c:if>>
                                    ${paymentMethod}
                            </option>
                        </c:forEach>
                    </select>
                    <c:set var="error" value="${errors['paymentMethod']}"></c:set>
                    <c:if test="${not empty error}">
                        <p style="color: red;">${error}</p>
                    </c:if>
                </td>
            </tr>
        </table>
        <p>
            <button>Place order</button>
        </p>
    </form>
</tags:master>
