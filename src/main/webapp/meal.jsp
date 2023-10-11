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
           value=${meal.id}> <br/>
    <label>DateTime:</label> <input type="datetime-local" name="dateTime"
                                    value=${meal.dateTime}> <br/>
    <label>Description:</label> <input type="text" name="description"
                                       value=${meal.description}> <br/>
    <label>Calories:</label> <input type="number" name="calories"
                                    value=${meal.calories}> <br/>
    <input type="submit" value="Save"/>
    <button onclick="window.history.back()" type="button">Cancel</button>
</form>
</body>
</html>