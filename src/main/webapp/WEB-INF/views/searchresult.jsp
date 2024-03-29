<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
</head>
<body>
    <h2>Searched result by text <i>${query}</i></h2>

    <c:forEach var="siteModel" items="${siteModels}">
        <div class="title"><b>${siteModel.title}</b></div>
        <div><a href=${siteModel.url}>${siteModel.url}</a></div>
        <br>
    </c:forEach>
    <script src="http://code.jquery.com/jquery-2.0.3.min.js"></script>
    <script type="text/javascript" src="/resources/js/app.js"></script>
</body>
</html>