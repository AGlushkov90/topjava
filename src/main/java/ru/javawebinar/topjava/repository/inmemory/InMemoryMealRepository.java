package ru.javawebinar.topjava.repository.inmemory;

import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.DateTimeUtil;
import ru.javawebinar.topjava.util.MealsUtil;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Repository
public class InMemoryMealRepository implements MealRepository {
    private final Map<Integer, Map<Integer, Meal>> repository = new ConcurrentHashMap<>();
    private final AtomicInteger counter = new AtomicInteger(0);

    {
        MealsUtil.meals.forEach(meal -> save(meal, meal.getUserId()));
    }

    @Override
    public Meal save(Meal meal, int userId) {
        if (meal.isNew()) {
            meal.setId(counter.incrementAndGet());
            meal.setUserId(userId);
            Map<Integer, Meal> mapMeals = repository.getOrDefault(userId, new ConcurrentHashMap<>());
            mapMeals.put(meal.getId(), meal);
            repository.put(userId, mapMeals);
            return meal;
        }
        Map<Integer, Meal> mapMeals = repository.get(userId);
        if (!isExistUser(mapMeals) || !isMealBelongsToCurrentUser(meal.getId(), userId, mapMeals)) {
            return null;
        }
        // handle case: update, but not present in storage
        meal.setUserId(userId);
        mapMeals.computeIfPresent(meal.getId(), (id, oldMeal) -> meal);
        repository.computeIfPresent(userId, (currentUserId, oldMapMeals) -> mapMeals);
        return meal;
    }

    @Override
    public boolean delete(int id, int userId) {
        Map<Integer, Meal> mapMeals = repository.get(userId);
        if (!isExistUser(mapMeals) || !isMealBelongsToCurrentUser(id, userId, mapMeals)) {
            return false;
        }
        return mapMeals.remove(id) != null;
    }

    @Override
    public Meal get(int id, int userId) {
        Map<Integer, Meal> mapMeals = repository.get(userId);
        if (!isExistUser(mapMeals)) {
            return null;
        }
        Meal meal = mapMeals.get(id);
        if (!isMealBelongsToCurrentUser(meal, userId)) {
            return null;
        }
        return meal;
    }

    @Override
    public List<Meal> getAll(int userId) {
        return getByPredicate(meal -> true, userId);
    }

    @Override
    public List<Meal> getBetween(LocalDate endDate, LocalDate startDate, int userId) {
        return getByPredicate(meal -> DateTimeUtil.isBetween(meal.getDate(), startDate, endDate), userId);
    }

    private boolean isMealBelongsToCurrentUser(int id, int userId, Map<Integer, Meal> mapMeals) {
        return isMealBelongsToCurrentUser(mapMeals.get(id), userId);
    }

    private boolean isMealBelongsToCurrentUser(Meal meal, int userId) {
        return meal != null && meal.getUserId().equals(userId);
    }

    private List<Meal> getByPredicate(Predicate<Meal> filter, int userId) {
        return repository.get(userId).values().stream()
                .filter(filter)
                .sorted(Comparator.comparing(Meal::getDateTime).reversed())
                .collect(Collectors.toList());
    }

    private boolean isExistUser(Map<Integer, Meal> mapMeals) {
        return mapMeals != null;
    }
}

