package ru.javawebinar.topjava.dao;

import ru.javawebinar.topjava.model.Meal;


public interface MealDao {
    void addMeal(Meal meal);

    void deleteMeal(Integer id);

    void updateMeal(Meal meal);

    Meal getMealById(Integer mealId);
}
