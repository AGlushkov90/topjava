<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<html lang="ru">
<head>
    <title>Meals</title>
    <style>
        TABLE {
            border-collapse: collapse;
        }

        TD, TH {
            border: 1px solid black;
        }
    </style>
</head>
<body>
<h3><a href="index.html">Home</a></h3>
<hr>
<h2>Meals</h2>

<table>
    <tbody>
    <p><a href="meals?action=insert">Add Meal</a></p>
    <tr>
        <td style="text-align:center;">Date</td>
        <td style="text-align:center;">Description</td>
        <td style="text-align:center;">Calories</td>
        <td style="text-align:center;"></td>
        <td style="text-align:center;"></td>
    </tr>
    <c:forEach items="${mealsTo}" var="meal">
        <tr style="color:${meal.excess ? 'red' : 'green'}; text-align:center">
            <fmt:parseDate value="${meal.dateTime}" pattern="yyyy-MM-dd'T'HH:mm" var="parsedDateTime" type="both"/>
            <td><fmt:formatDate pattern="yyyy-MM-dd HH:mm" value="${parsedDateTime}"/></td>
            <td>${meal.description}</td>
            <td>${meal.calories}</td>
            <td><a href="meals?action=edit&mealId=${meal.id}">Update</a></td>
            <td><a href="meals?action=delete&mealId=${meal.id}">Delete</a></td>
        </tr>
    </c:forEach>
    </tbody>
</table>
</body>
</html>