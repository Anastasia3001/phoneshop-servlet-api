<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<jsp:useBean id="products" type="java.util.ArrayList" scope="request"/>
<tags:master pageTitle="Advanced Search">
    <h1 class="title">Advanced search</h1>
    <table style="border: none;">
        <tr>
            <td>Description</td>
            <td>
                <c:set var="error" value="${errors['description']}"></c:set>
                <input name=${'description'} value="${not empty error ? param['description'] : product['description']}">
                <c:if test="${not empty error}">
                    <p style="color: red;">${error}</p>
                </c:if>
                <select name="searchingType">
                    <c:forEach var="searchingType" items="${searchingType}">
                        <option value="${searchingType}"
                                <c:if test="${searchingType eq param['searchingType']}">
                                    selected
                                </c:if>>
                                ${searchingType}
                        </option>
                    </c:forEach>
                </select>
            </td>
        </tr>
        <tr>
            <td>Min price</td>
            <td>
                <c:set var="error" value="${errors['minPrice']}"></c:set>
                <input name=${'minPrice'} value="${not empty error ? param['minPrice'] : product['minPrice']}">
                <c:if test="${not empty error}">
                    <p style="color: red;">${error}</p>
                </c:if>
            </td>
        </tr>
        <tr>
            <td>Max price</td>
            <td>
                <c:set var="error" value="${errors['maxPrice']}"></c:set>
                <input name=${'maxPrice'} value="${not empty error ? param['maxPrice'] : product['maxPrice']}">
                <c:if test="${not empty error}">
                    <p style="color: red;">${error}</p>
                </c:if>
            </td>
        </tr>
    </table>
    <form>
        <button>Search</button>
    </form>
        <table>
            <thead>
            <tr>
                <td>Image</td>
                <td>Description</td>
                <td class="price">Price</td>
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
                        <td class="price">
                            <a href='#'
                               onclick='javascript:window.open("${pageContext.servletContext.contextPath}/priceHistory/${product.id}", "_blank", "scrollbars=1,resizable=1,height=300,width=450");'
                               title='Pop Up'>
                                <fmt:formatNumber value="${product.price}" type="currency"
                                                  currencySymbol="${product.currency.symbol}"/>
                            </a>
                        </td>
                    </tr>
                </form>
            </c:forEach>
        </table>
</tags:master>
