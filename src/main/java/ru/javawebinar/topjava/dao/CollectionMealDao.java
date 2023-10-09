package ru.javawebinar.topjava.dao;

import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class CollectionMealDao implements MealDao {
    private static final List<Meal> meals;
    private static final AtomicInteger counter;

    static {
        meals = Collections.synchronizedList(new ArrayList<>(Arrays.asList(
                new Meal(LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500, 1),
                new Meal(LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Обед", 1000, 2),
                new Meal(LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Ужин", 500, 3),
                new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Еда на граничное значение", 100, 4),
                new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Завтрак", 1000, 5),
                new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "Обед", 500, 6),
                new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 23, 59, 59), "Ужин", 410, 7))));
        counter = new AtomicInteger(meals.size());
    }

    @Override
    public void addMeal(Meal meal) {
        meal.setId(counter.incrementAndGet());
        CollectionMealDao.meals.add(meal);
    }

    @Override
    public void deleteMeal(Integer mealId) {
        Optional<Meal> OptionalMeal = meals.stream().filter(meal -> meal.getId().equals(mealId)).findFirst();
        OptionalMeal.ifPresent(meals::remove);
    }

    @Override
    public void updateMeal(Meal meal) {
        meals.set(meals.indexOf(getMealById(meal.getId())), meal);
    }

    @Override
    public Meal getMealById(Integer mealId) {
        return meals.stream().filter(meal -> meal.getId().equals(mealId)).findFirst().get();
    }

    public static List<Meal> getAllMeals() {
        return meals;
    }
}

