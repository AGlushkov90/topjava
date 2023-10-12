package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
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

import static org.slf4j.LoggerFactory.getLogger;

public class MealServlet extends HttpServlet {
    private static final String INSERT_OR_EDIT = "/meal.jsp";
    private static final String LIST_MEAL = "/meals.jsp";
    private static final Logger log = getLogger(UserServlet.class);
    private MealDao dao;

    @Override
    public void init() throws ServletException {
        dao = new CollectionMealDao();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String forward;
        String action = String.valueOf(request.getParameter("action"));
        int mealId;
        switch (action) {
            case ("delete"):
                mealId = Integer.parseInt(request.getParameter("mealId"));
                log.debug("delete meal id: {}", mealId);
                dao.delete(mealId);
                response.sendRedirect(getServletContext().getContextPath() + "/meals");
                return;
            case ("edit"):
                forward = INSERT_OR_EDIT;
                mealId = Integer.parseInt(request.getParameter("mealId"));
                log.debug("edit meal id: {}", mealId);
                Meal meal = dao.getById(mealId);
                request.setAttribute("meal", meal);
                break;
            case ("insert"):
                log.debug("add meal");
                forward = INSERT_OR_EDIT;
                break;
            default:
                log.debug("open meals");
                forward = LIST_MEAL;
                request.setAttribute("mealsTo", MealsUtil.filteredByStreams(dao.getAll(), MealsUtil.CALORIES_PER_DAY));
                break;
        }
        request.getRequestDispatcher(forward).forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        Meal meal = new Meal(LocalDateTime.parse(request.getParameter("dateTime")), request.getParameter("description"),
                Integer.parseInt(request.getParameter("calories")));
        String mealId = request.getParameter("mealId");
        if (mealId == null || mealId.isEmpty()) {
            dao.add(meal);
            log.debug("added meal id: {}", meal.getId());
        } else {
            meal.setId(Integer.parseInt(mealId));
            dao.update(meal);
            log.debug("edited meal id: {}", mealId);
        }
        response.sendRedirect(getServletContext().getContextPath() + "/meals");
    }
}
