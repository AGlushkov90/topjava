package ru.javawebinar.topjava.service;

import org.springframework.stereotype.Service;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;

import java.util.List;

import static ru.javawebinar.topjava.util.ValidationUtil.checkNotFoundWithId;

@Service
public class MealService {

    private final MealRepository repository;

    public MealService(MealRepository repository) {
        this.repository = repository;
    }

    public List<Meal> getAll(int userId) {
        return (List<Meal>) repository.getAll(userId);
    }

    public Meal createWithLocation(Meal meal) {
        return repository.save(meal);
    }

    public Meal get(int id, int idUser) {
        return checkNotFoundWithId(repository.get(id, idUser), id);
    }

    public void update(Meal meal) {
        checkNotFoundWithId(repository.save(meal), meal.getId());
    }

    public void delete(int id, int idUser) {
        checkNotFoundWithId(repository.delete(id, idUser), id);
    }

    public List<Meal> getBetween(String endDate, String startDate, int userId) {
        return (List<Meal>) repository.getBetween(endDate, startDate, userId);
    }
}