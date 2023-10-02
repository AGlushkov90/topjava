package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.model.UserMealWithExcess;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class UserMealsUtil {
    public static void main(String[] args) {
        List<UserMeal> meals = Arrays.asList(
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Обед", 1000),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Ужин", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Еда на граничное значение", 100),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Завтрак", 1000),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "Обед", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Ужин", 410)
        );

        List<UserMealWithExcess> mealsTo = filteredByCycles(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000);
        mealsTo.forEach(System.out::println);

        System.out.println(filteredByStreams(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000));
    }

    public static List<UserMealWithExcess> filteredByCycles(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        int sumCalories = 0;
        List<UserMealWithExcess> userMealWithExcessList = new ArrayList<>();
        for (UserMeal userMeal : meals) {
            LocalTime localTime = userMeal.getDateTime().toLocalTime();
            if (TimeUtil.isBetweenHalfOpen(localTime, startTime, endTime)) {
                userMealWithExcessList.add(new UserMealWithExcess(userMeal));
                sumCalories += userMeal.getCalories();
            }
        }
        if (sumCalories > caloriesPerDay) {
            for (UserMealWithExcess userMealWithExcess : userMealWithExcessList) {
                userMealWithExcess.setExcess(true);
            }
        }
        return userMealWithExcessList;
    }

    public static List<UserMealWithExcess> filteredByStreams(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        int sumCalories = meals.stream()
                .filter(a -> (TimeUtil.isBetweenHalfOpen(a.getDateTime().toLocalTime(), startTime, endTime)))
                .reduce(0, (x, y) -> x + y.getCalories(), Integer::sum);
        return meals.stream()
                .filter(a -> (TimeUtil.isBetweenHalfOpen(a.getDateTime().toLocalTime(), startTime, endTime)))
                .map(a -> new UserMealWithExcess(a, sumCalories > caloriesPerDay))
                .collect(Collectors.toList());
    }
}
