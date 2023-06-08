<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<jsp:useBean id="products" type="java.util.ArrayList" scope="request"/>
<tags:master pageTitle="Product List">
    <p>
        Welcome to Expert-Soft training!
    </p>
    <c:if test="${not empty param.message && empty error}">
        <p class="message">
                ${param.message}
        </p>
    </c:if>
    <c:if test="${not empty error}">
        <p class="errorGeneral">
            An error occurred while adding to the cart
        </p>
    </c:if>
    <form>
        <input type="text" name="description" value="${param.description}">
        <button>Search</button>
    </form>
    <form action="${pageContext.servletContext.contextPath}/products/advancedSearch">
        <button>Advanced search</button>
    </form>
    <table>
        <thead>
        <tr>
            <td>Image</td>
            <td>
                Description
                <tags:sortLink sorting="DESCRIPTION" type="ASC"></tags:sortLink>
                <tags:sortLink sorting="DESCRIPTION" type="DESC"></tags:sortLink>
            </td>
            <td>Quantity</td>
            <td class="price">
                Price
                <tags:sortLink sorting="PRICE" type="ASC"></tags:sortLink>
                <tags:sortLink sorting="PRICE" type="DESC"></tags:sortLink>
            </td>
            <td></td>
        </tr>
        </thead>
        <c:forEach var="product" items="${products}">
            <form method="post" action="${pageContext.servletContext.contextPath}/products/addCartItem/${product.id}">
            <tr>
                <td>
                    <img class="product-tile" src="${product.imageUrl}">
                </td>
                <td>
                    <a href="${pageContext.servletContext.contextPath}/products/${product.id}">
                            ${product.description}
                    </a>
                </td>
                <td>
                    <c:set var="err" value="${error[product.id]}"/>
                    <input name="quantity" value="${not empty err ? param.quantity : 1}">
                    <c:if test="${not empty err}">
                        <p class="error">
                                ${error[product.id]}
                        </p>
                    </c:if>
                    <input name="productId" type="hidden" value="${product.id}">
                </td>
                <td class="price">
                    <a href='#'
                       onclick='javascript:window.open("${pageContext.servletContext.contextPath}/priceHistory/${product.id}", "_blank", "scrollbars=1,resizable=1,height=300,width=450");'
                       title='Pop Up'>
                        <fmt:formatNumber value="${product.price}" type="currency"
                                          currencySymbol="${product.currency.symbol}"/>
                    </a>
                </td>
                <td>
                    <button>Add to cart</button>
                </td>
            </tr>
            </form>
        </c:forEach>
    </table>
    <h1>Recently viewed</h1>
    <div id="mainDiv">
        <c:forEach var="recentlyViewedProduct" items="${recentlyViewedProducts}">
            <table>
                <tr>
                    <td><img src="${recentlyViewedProduct.imageUrl}"></td>
                </tr>
                <tr>
                    <td><a href="${pageContext.servletContext.contextPath}/products/${recentlyViewedProduct.id}">
                            ${recentlyViewedProduct.description}
                    </a></td>
                </tr>
                <tr>
                    <td><fmt:formatNumber value="${recentlyViewedProduct.price}" type="currency"
                                          currencySymbol="${recentlyViewedProduct.currency.symbol}"/></td>
                </tr>
            </table>
        </c:forEach>
    </div>
</tags:master>
