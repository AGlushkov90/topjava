package ru.javawebinar.topjava.web;

import ru.javawebinar.topjava.dao.CollectionMealDao;
import ru.javawebinar.topjava.dao.MealDao;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.MealsUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;

public class MealServlet extends HttpServlet {

    private final MealDao dao;
    private static final String INSERT_OR_EDIT = "/meal.jsp";
    private static final String LIST_MEAL = "/meals.jsp";

    public MealServlet() {
        super();
        dao = new CollectionMealDao();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String forward;
        String action = request.getParameter("action");
        if (action.equalsIgnoreCase("delete")) {
            int mealId = Integer.parseInt(request.getParameter("mealId"));
            dao.deleteMeal(mealId);
            forward = LIST_MEAL;
            request.setAttribute("mealsTo", MealsUtil.filteredByStreams(CollectionMealDao.getAllMeals()));
        } else if (action.equalsIgnoreCase("edit")) {
            forward = INSERT_OR_EDIT;
            int mealId = Integer.parseInt(request.getParameter("mealId"));
            Meal meal = dao.getMealById(mealId);
            request.setAttribute("meal", meal);
        } else if (action.equalsIgnoreCase("listMeal")) {
            forward = LIST_MEAL;
            request.setAttribute("mealsTo", MealsUtil.filteredByStreams(CollectionMealDao.getAllMeals()));
        } else {
            forward = INSERT_OR_EDIT;
        }
        request.getRequestDispatcher(forward).forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        Meal meal = new Meal(LocalDateTime.parse(request.getParameter("dateTime")), request.getParameter("description"), Integer.parseInt(request.getParameter("calories")));
        String mealId = request.getParameter("mealId");
        if (mealId == null || mealId.isEmpty()) {
            dao.addMeal(meal);
        } else {
            meal.setId(Integer.parseInt(mealId));
            dao.updateMeal(meal);
        }
        request.setAttribute("mealsTo", MealsUtil.filteredByStreams(CollectionMealDao.getAllMeals()));
        request.getRequestDispatcher(LIST_MEAL).forward(request, response);
    }
}
