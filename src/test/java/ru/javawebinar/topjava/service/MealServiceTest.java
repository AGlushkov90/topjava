package ru.javawebinar.topjava.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.bridge.SLF4JBridgeHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit4.SpringRunner;
import ru.javawebinar.topjava.MealTestData;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertThrows;
import static ru.javawebinar.topjava.MealTestData.*;
import static ru.javawebinar.topjava.UserTestData.*;

@ContextConfiguration({
        "classpath:spring/spring-app.xml",
        "classpath:spring/spring-db.xml",
        "classpath:spring/spring.xml"
})
@RunWith(SpringRunner.class)
@Sql(scripts = "classpath:db/populateDB.sql", config = @SqlConfig(encoding = "UTF-8"))
public class MealServiceTest {

    static {
        // Only for postgres driver logging
        // It uses java.util.logging and logged via jul-to-slf4j bridge
        SLF4JBridgeHandler.install();
    }

    @Autowired
    private MealService service;

    @Test
    public void get() {
        Meal meal = service.get(MEAL_ID, USER_ID);
        asserMatch(meal, userMeal);
    }

    @Test
    public void getNotFound() {
        assertThrows(NotFoundException.class, () -> service.get(MealTestData.NOT_FOUND, ADMIN_ID));
    }

    @Test
    public void delete() {
        service.delete(MEAL_ID, USER_ID);
        assertThrows(NotFoundException.class, () -> service.get(MEAL_ID, USER_ID));
    }

    @Test
    public void deletedNotFound() {
        assertThrows(NotFoundException.class, () -> service.delete(MealTestData.NOT_FOUND, ADMIN_ID));
    }

    @Test
    public void getBetweenInclusive() {
        List<Meal> all = service.getBetweenInclusive(LocalDate.of(2023, 10, 31),
                LocalDate.of(2023, 10, 31), USER_ID);
        asserMatch(all, Arrays.asList(userMeal6, userMeal5, userMeal4, userMeal3));
    }

    @Test
    public void getWithEmptyRange() {
        List<Meal> all = service.getBetweenInclusive(null, null, ADMIN_ID);
        asserMatch(all, Arrays.asList(adminMeal1, adminMeal));
    }

    @Test
    public void getAll() {
        List<Meal> all = service.getAll(ADMIN_ID);
        asserMatch(all, Arrays.asList(adminMeal1, adminMeal));
    }

    @Test
    public void update() {
        Meal updated = MealTestData.getUpdate();
        service.update(updated, USER_ID);
        asserMatch(service.get(MEAL_ID, USER_ID), MealTestData.getUpdate());
    }

    @Test
    public void create() {
        Meal created = service.create(MealTestData.getNew(), ADMIN_ID);
        Integer newId = created.getId();
        Meal newMeal = MealTestData.getNew();
        newMeal.setId(newId);
        asserMatch(created, newMeal);
        asserMatch(service.get(newId, ADMIN_ID), newMeal);
    }

    @Test
    public void duplicateDateTimeCreate() {
        assertThrows(DataAccessException.class, () ->
                service.create(new Meal(null, userMeal.getDateTime(),
                        "ужин юзер", 100), USER_ID));
    }

    @Test
    public void deletedByAnotherUser() {
        assertThrows(NotFoundException.class, () -> service.delete(MEAL_ID, ADMIN_ID));
    }

    @Test
    public void getByAnotherUser() {
        assertThrows(NotFoundException.class, () -> service.get(MEAL_ID, ADMIN_ID));
    }

    @Test
    public void updateByAnotherUser() {
        Meal updated = MealTestData.getUpdate();
        assertThrows(NotFoundException.class, () -> service.update(updated, ADMIN_ID));
    }
}