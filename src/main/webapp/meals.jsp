<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<html lang="ru">
<head>
    <title>Meals</title>
    <style>
        TABLE {
            border-collapse: collapse; /* Убираем двойные линии между ячейками */
        }

        TD, TH {
            border: 1px solid black; /* Параметры рамки */
        }
    </style>
</head>
<body>
<h3><a href="index.html">Home</a></h3>
<hr>
<jsp:useBean id="mealsTo" class="java.util.ArrayList" scope="request"/>

<table>
    <tbody>
    <tr>
        <td style="text-align:center;">Date</td>
        <td style="text-align:center;">Description</td>
        <td style="text-align:center;">Calories</td>
    </tr>
    <c:forEach items="${mealsTo}" var="cell">
        <tr style="color:${cell.isExcess() ? 'red' : 'green'}">
            <fmt:parseDate value="${cell.getDateTime()}" pattern="yyyy-MM-dd'T'HH:mm" var="parsedDateTime" type="both"/>
            <td style="text-align:center;"><fmt:formatDate pattern="dd.MM.yyyy HH:mm" value="${parsedDateTime}"/></td>
            <td style="text-align:center;">${cell.getDescription()}</td>
            <td style="text-align:center;">${cell.getCalories()}</td>
        </tr>
    </c:forEach>
    </tbody>
</table>
</body>
</html>