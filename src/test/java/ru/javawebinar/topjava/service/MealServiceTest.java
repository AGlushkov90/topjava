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
import ru.javawebinar.topjava.UserTestData;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertThrows;
import static ru.javawebinar.topjava.MealTestData.*;
import static ru.javawebinar.topjava.UserTestData.*;

@ContextConfiguration({
        "classpath:spring/spring-app.xml",
        "classpath:spring/spring-db.xml"
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
        assertThat(meal).usingRecursiveComparison().isEqualTo(MealTestData.meal);
    }

    @Test
    public void getNotFound() {
        assertThrows(NotFoundException.class, () -> service.get(MealTestData.NOT_FOUND, UserTestData.NOT_FOUND));
    }

    @Test
    public void delete() {
        service.delete(MEAL_ID, USER_ID);
        assertThrows(NotFoundException.class, () -> service.get(MEAL_ID, USER_ID));
    }

    @Test
    public void deletedNotFound() {
        assertThrows(NotFoundException.class, () -> service.delete(MealTestData.NOT_FOUND, UserTestData.NOT_FOUND));
    }

    @Test
    public void getBetweenInclusive() {
        List<Meal> all = service.getBetweenInclusive(LocalDate.of(2023, 10, 29),
                LocalDate.of(2023, 10, 30), ADMIN_ID);
        assertThat(all).usingRecursiveComparison().isEqualTo(Collections.singletonList(meal7));
    }

    @Test
    public void getAll() {
        List<Meal> all = service.getAll(ADMIN_ID);
        assertThat(all).usingRecursiveComparison().isEqualTo(Arrays.asList(meal8, meal7));
    }

    @Test
    public void update() {
        Meal updated = MealTestData.getUpdate();
        service.update(updated, USER_ID);
        assertThat(service.get(MEAL_ID, USER_ID)).usingRecursiveComparison().isEqualTo(MealTestData.getUpdate());

    }

    @Test
    public void create() {
        Meal created = service.create(MealTestData.getNew(), ADMIN_ID);
        Integer newId = created.getId();
        Meal newMeal = MealTestData.getNew();
        newMeal.setId(newId);
        assertThat(created).usingRecursiveComparison().isEqualTo(newMeal);
        assertThat(service.get(newId, ADMIN_ID)).usingRecursiveComparison().isEqualTo(newMeal);
    }

    @Test
    public void duplicateDateTimeCreate() {
        assertThrows(DataAccessException.class, () ->
                service.create(new Meal(null, LocalDateTime.parse("2023-10-30T13:00:00"),
                        "ужин юзер", 100), USER_ID));
    }

    @Test
    public void deletedNotFoundUserID() {
        assertThrows(NotFoundException.class, () -> service.delete(MEAL_ID, UserTestData.NOT_FOUND));
    }

    @Test
    public void getNotFoundUserID() {
        assertThrows(NotFoundException.class, () -> service.get(MEAL_ID, UserTestData.NOT_FOUND));
    }
}