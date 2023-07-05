<%@ tag trimDirectiveWhitespaces="true" %>
<%@ attribute name="name" required="true" %>
<%@ attribute name="label" required="true" %>
<%@ attribute name="errors" required="true" type="java.util.Map" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<td>${label}</td>
<td>
    <c:set var="error" value="${errors[name]}"></c:set>
    <input name=${name} value="${param[name]}">
    <c:if test="${not empty error}">
        <p style="color: red;">${error}</p>
    </c:if>
</td>
<td></td>