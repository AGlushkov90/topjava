package ru.javawebinar.topjava.web.meal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Controller;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.to.MealTo;
import ru.javawebinar.topjava.util.MealsUtil;
import ru.javawebinar.topjava.web.SecurityUtil;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static ru.javawebinar.topjava.util.ValidationUtil.assureIdConsistent;
import static ru.javawebinar.topjava.util.ValidationUtil.checkNew;

@Controller

public class MealRestController {

    protected final Logger log = LoggerFactory.getLogger(getClass());

    private final MealService service;

    public MealRestController(MealService service) {
        this.service = service;
    }

    public List<MealTo> getAll() {
        log.info("getAll");
        return MealsUtil.getTos(service.getAll(SecurityUtil.authUserId()), MealsUtil.DEFAULT_CALORIES_PER_DAY);
    }

    public Meal createWithLocation(Meal meal) {
        log.info("create {}", meal);
        checkNew(meal);
        return service.createWithLocation(meal);
    }

    public Meal get(int id) {
        log.info("get {}", id);
        return service.get(id, SecurityUtil.authUserId());
    }

    public void update(Meal meal, int id) {
        log.info("update {} with id={}", meal, id);
        assureIdConsistent(meal, id);
        service.update(meal);
    }

    public void delete(int id) {
        log.info("delete {}", id);
        service.delete(id, SecurityUtil.authUserId());
    }

    public List<MealTo> getBetween(String endDate, String endTime, String startDate, String startTime) {
        log.info("getBetween {}-{} {}-{}", endDate, endTime, startDate, startTime);
        final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");
        LocalTime localStartTime = startTime.equals("") ? LocalTime.MIN : LocalTime.parse(startTime, TIME_FORMATTER);
        LocalTime localEndTime = endTime.equals("") ? LocalTime.MAX : LocalTime.parse(endTime, TIME_FORMATTER);
        return MealsUtil.getFilteredTos(service.getBetween(endDate, startDate, SecurityUtil.authUserId()), MealsUtil.DEFAULT_CALORIES_PER_DAY,
                localStartTime, localEndTime);
    }


}