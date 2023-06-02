<%@ tag trimDirectiveWhitespaces="true" %>
<%@ attribute name="name" required="true" %>
<%@ attribute name="order" required="true" type="com.es.phoneshop.model.Order" %>
<%@ attribute name="label" required="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<tr>
    <td>${label}</td>
    <td>
        ${order[name]}
    </td>
</tr>