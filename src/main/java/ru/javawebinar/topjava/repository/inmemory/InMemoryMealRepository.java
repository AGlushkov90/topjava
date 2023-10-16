package ru.javawebinar.topjava.repository.inmemory;

import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.DateTimeUtil;
import ru.javawebinar.topjava.util.MealsUtil;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.Comparator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Repository
public class InMemoryMealRepository implements MealRepository {
    private final Map<Integer, Meal> repository = new ConcurrentHashMap<>();
    private final AtomicInteger counter = new AtomicInteger(0);

    {
        MealsUtil.meals.forEach(this::save);
    }

    @Override
    public Meal save(Meal meal) {
        if (meal.isNew()) {
            meal.setId(counter.incrementAndGet());
            repository.put(meal.getId(), meal);
            return meal;
        }
        // handle case: update, but not present in storage
        if (!isMealCurrentUser(meal.getId(), meal.getUserId())) {
            return null;
        }
        return repository.computeIfPresent(meal.getId(), (id, oldMeal) -> meal);
    }

    @Override
    public boolean delete(int id, int userId) {
        if (!isMealCurrentUser(id, userId)) {
            return false;
        }
        return repository.remove(id) != null;
    }

    @Override
    public Meal get(int id, int userId) {
        if (!isMealCurrentUser(id, userId)) {
            return null;
        }
        return repository.get(id);
    }

    @Override
    public Collection<Meal> getAll(int userId) {
        return repository.values().stream().filter(meal -> meal.getUserId().equals(userId))
                .sorted(Comparator.comparing(Meal::getDateTime).reversed()).collect(Collectors.toList());
    }

    @Override
    public Collection<Meal> getBetween(String endDate, String startDate, int userId) {
        final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate localStartDate = startDate.equals("") ? LocalDate.MIN : LocalDate.parse(startDate, DATE_TIME_FORMATTER);
        LocalDate localEndDate = endDate.equals("") ? LocalDate.MAX : LocalDate.parse(endDate, DATE_TIME_FORMATTER);
        return getAll(userId).stream().filter(meal -> DateTimeUtil.isBetweenHalfOpen(meal.getDate(), localStartDate, localEndDate))
                .collect(Collectors.toList());
    }

    private boolean isMealCurrentUser(Integer id, Integer userId) {
        for (Map.Entry<Integer, Meal> entry : repository.entrySet()) {
            if (entry.getKey().equals(id) && entry.getValue().getUserId().equals(userId)) {
                return true;
            }
        }
        return false;
    }
}

