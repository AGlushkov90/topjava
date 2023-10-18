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
    private final Map<Integer, Meal> repository = new ConcurrentHashMap<>();
    private final AtomicInteger counter = new AtomicInteger(0);

    {
        MealsUtil.meals.forEach(meal -> save(meal, meal.getUserId()));
    }

    @Override
    public Meal save(Meal meal, int userId) {
        if (meal.isNew()) {
            meal.setId(counter.incrementAndGet());
            meal.setUserId(userId);
            repository.put(meal.getId(), meal);
            return meal;
        }
        // handle case: update, but not present in storage
        if (!isMealBelongsToCurrentUser(meal.getId(), userId)) {
            return null;
        }
        meal.setUserId(userId);
        return repository.computeIfPresent(meal.getId(), (id, oldMeal) -> meal);
    }

    @Override
    public boolean delete(int id, int userId) {
        if (!isMealBelongsToCurrentUser(id, userId)) {
            return false;
        }
        return repository.remove(id) != null;
    }

    @Override
    public Meal get(int id, int userId) {
        Meal meal = repository.get(id);
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

    private boolean isMealBelongsToCurrentUser(int id, int userId) {
        Meal meal = repository.get(id);
        return isMealBelongsToCurrentUser(meal, userId);
    }

    private boolean isMealBelongsToCurrentUser(Meal meal, int userId) {
        return meal != null && meal.getUserId().equals(userId);
    }

    private List<Meal> getByPredicate(Predicate<Meal> filter, int userId) {
        return repository.values().stream()
                .filter(meal -> meal.getUserId().equals(userId))
                .filter(filter)
                .sorted(Comparator.comparing(Meal::getDateTime).reversed())
                .collect(Collectors.toList());
    }
}

