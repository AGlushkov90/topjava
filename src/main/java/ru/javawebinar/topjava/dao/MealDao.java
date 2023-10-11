package ru.javawebinar.topjava.dao;

import ru.javawebinar.topjava.model.Meal;

import java.util.List;


public interface MealDao {
    Meal add(Meal meal);

    void delete(int id);

    Meal update(Meal meal);

    Meal getById(int id);

    List<Meal> getAll();
}
