<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<html lang="ru">
<head>

    <title>Add new meal</title>
    <style>
        label {
            display: block;
            float: left;
            width: 150px;
            height: 15px;
        }

        input {
            margin-bottom: 10px;
        }
    </style>
</head>
<body>
<h3>${meal == NULL ? 'Add meal' : 'Edit meal'}</h3>
<form method="POST" action='meals' name="frmAddMeal">
    <input type="hidden" name="mealId"
           value="<c:out value="${meal.getId()}" />"/> <br/>
    <label>DateTime:</label> <input type="datetime-local" name="dateTime"
                                    value="<c:out value="${meal.getDateTime()}" />"/> <br/>
    <label>Description:</label> <input type="text" name="description"
                                       value="<c:out value="${meal.getDescription()}" />"/> <br/>
    <label>Calories:</label> <input type="text" name="calories"
                                    value="<c:out value="${meal.getCalories()}" />"/> <br/>
    <input type="submit" value="Save"/>
    <button onclick="window.history.back()" type="button">Cancel</button>
</form>
</body>
</html>