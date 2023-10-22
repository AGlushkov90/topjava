package ru.javawebinar.topjava;

import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;

import static ru.javawebinar.topjava.model.AbstractBaseEntity.START_SEQ;

public class MealTestData {
    public static final int MEAL_ID = START_SEQ + 3;
    public static final int MEAL1_ID = START_SEQ + 4;
    public static final int MEAL2_ID = START_SEQ + 5;
    public static final int MEAL3_ID = START_SEQ + 6;
    public static final int MEAL4_ID = START_SEQ + 7;
    public static final int MEAL5_ID = START_SEQ + 8;
    public static final int MEAL6_ID = START_SEQ + 9;
    public static final int MEAL7_ID = START_SEQ + 10;
    public static final int MEAL8_ID = START_SEQ + 11;
    public static final int NOT_FOUND = 11;
    public static final Meal meal = new Meal(MEAL_ID, LocalDateTime.parse("2023-10-30T10:00:00"), "Завтрак юзер", 500);
    public static final Meal meal1 = new Meal(MEAL1_ID, LocalDateTime.parse("2023-10-30T13:00:00"), "Обед юзер", 1000);
    public static final Meal meal2 = new Meal(MEAL2_ID, LocalDateTime.parse("2023-10-30T20:00:00"), "Ужин юзер", 500);
    public static final Meal meal3 = new Meal(MEAL3_ID, LocalDateTime.parse("2023-10-31T00:00:00"), "Еда на граничное значение юзер", 100);
    public static final Meal meal4 = new Meal(MEAL4_ID, LocalDateTime.parse("2023-10-31T10:00:00"), "Завтрак юзер", 1000);
    public static final Meal meal5 = new Meal(MEAL5_ID, LocalDateTime.parse("2023-10-31T13:00:00"), "Обед юзер", 500);
    public static final Meal meal6 = new Meal(MEAL6_ID, LocalDateTime.parse("2023-10-31T20:00:00"), "Ужин юзер", 410);
    public static final Meal meal7 = new Meal(MEAL7_ID, LocalDateTime.parse("2023-10-29T10:00:00"), "Завтрак админ", 500);
    public static final Meal meal8 = new Meal(MEAL8_ID, LocalDateTime.parse("2023-10-31T12:00:00"), "ужин админ", 700);

    public static Meal getUpdate() {
        Meal update = new Meal(meal);
        update.setCalories(1000);
        return update;
    }

    public static Meal getNew() {
        return new Meal(null, LocalDateTime.MIN, "обед админ", 300);
    }
}
